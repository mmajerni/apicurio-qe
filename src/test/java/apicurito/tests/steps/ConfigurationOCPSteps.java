package apicurito.tests.steps;

import static org.junit.Assert.fail;

import static org.assertj.core.api.Assertions.assertThat;

import io.syndesis.qe.marketplace.openshift.OpenShiftConfiguration;
import io.syndesis.qe.marketplace.openshift.OpenShiftService;
import io.syndesis.qe.marketplace.openshift.OpenShiftUser;
import io.syndesis.qe.marketplace.quay.QuayService;
import io.syndesis.qe.marketplace.quay.QuayUser;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import apicurito.tests.configuration.Component;
import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.configuration.templates.ApicuritoTemplate;
import apicurito.tests.utils.openshift.OpenShiftUtils;
import apicurito.tests.utils.slenide.ConfigurationOCPUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigurationOCPSteps {

    @When("check that apicurito has {int} pods")
    public void checkThatApicuritoHasPods(int count) {
        log.info("Checking that Apicurito has exatly " + count + " pods");
        List<Pod> pods = OpenShiftUtils.getInstance().getPods();
        int counter = 0;
        for (Pod pod : pods) {
            if (pod.getMetadata().getName().contains(Component.SERVICE.getName()) && pod.getStatus().getPhase().equals("Running")) {
                ++counter;
            }
        }
        assertThat(count).as("Apicurito should have %s pods but currently run %s", count, counter).isEqualTo(counter);
    }

    @When("deploy {string} custom resource")
    public void deployCustomResource(String sequenceNumber) {
        log.info("Deploying " + sequenceNumber + " custom resource");
        String cr =
            "https://gist.githubusercontent.com/mmajerni/e47e14f2a1c2bf934219cb3d4508e81c/raw/749535da7044dcdb8f35c0d9aa88d4f25a444f43/" +
                "operatorUpdateTest.yaml";

        ConfigurationOCPUtils.applyInOCP("Custom Resource", cr);

        log.info("Waiting for Apicurito pods");
        if ("first".equals(sequenceNumber)) {
            ApicuritoTemplate.waitForApicurito("component", 8, Component.SERVICE);
        }else{
            ConfigurationOCPUtils.waitForRollout();
        }
    }

    @When("deploy another operator with ui image {string}")
    public void deployAnotherOperator(String uiImage) {
        log.info("Deploying another operator with UI image: " + uiImage);
        String operator =
            "https://gist.githubusercontent.com/mmajerni/e7a4b5287f92c7ef228ba655883048be/raw/1415804484dd9705b5ffecccbbf586aa5496ce3b/" +
                "apicuritoOperatorUpdate.yaml";
        ConfigurationOCPUtils.applyInOCP("Operator", operator);
        ConfigurationOCPUtils.setTestEnvToOperator("RELATED_IMAGE_APICURITO", uiImage);
        ConfigurationOCPUtils.waitForOperatorUpdate();
    }

    /**
     * @param podType operator || image
     * @param value pod image
     */
    @When("check that apicurito {string} is {string}")
    public void checkThatApicuritoImageIs(String podType, String value) {
        log.info("Checking that Apicurito " + podType + " pod is created from image: " + value);
        String nameOfPod = "operator".equals(podType) ? Component.OPERATOR.getName() : Component.SERVICE.getName();

        List<Pod> pods = OpenShiftUtils.getInstance().getPods();
        String imageName = "";
        for (Pod pod : pods) {
            if (pod.getMetadata().getName().contains(nameOfPod)) {
                imageName = pod.getSpec().getContainers().get(0).getImage();
                break;
            }
        }
        assertThat(imageName).as("Apicurito has not container from %s", value).isEqualTo(value);
    }

    @Then("check that apicurito operator is deployed and in running state")
    public void checkThatApicuritoOperatorIsDeployedAndInRunningState() {
        log.info("Checking that operator is deployed and in running state");
        OpenShiftUtils.xtf().waiters()
            .areExactlyNPodsReady(1, "name", "apicurito-operator")
            .interval(TimeUnit.SECONDS, 2)
            .timeout(TimeUnit.MINUTES, 3)
            .waitFor();
    }

    @When("deploy operator from operatorhub")
    public void deployOperatorHub() {
        QuayUser quayUser = new QuayUser(
            TestConfiguration.getQuayUsername(),
            TestConfiguration.getQuayPassword(),
            TestConfiguration.getQuayNamespace(),
            TestConfiguration.getQuayToken()
        );

        QuayService quayService = new QuayService(
            quayUser,
            ConfigurationOCPUtils.getOperatorImage(),
            null
        );
        String quayProject = null;
        try {
            quayProject = quayService.createQuayProject();
        } catch (Exception e) {
            log.error("Could not create quay project", e);
            fail("Could not create quay project");
        }

        OpenShiftUser adminUser = new OpenShiftUser(
            TestConfiguration.openshiftUsername(),
            TestConfiguration.openshiftPassword(),
            TestConfiguration.openShiftUrl()
        );

        OpenShiftConfiguration ocpConfig = OpenShiftConfiguration.builder()
            .namespace(TestConfiguration.openShiftNamespace())
            .pullSecretName("apicurito-pullsecret")
            .pullSecret(TestConfiguration.apicuritoPullSecret())
            .quayOpsrcToken(TestConfiguration.getQuayOpsrcToken())
            .installedCSV(quayService.getInstalledCSV())
            .build();

        OpenShiftService ocpService = new OpenShiftService(
            TestConfiguration.getQuayNamespace(),
            quayService.getPackageName(),
            ocpConfig,
            adminUser,
            null
        );

        try {
            ocpService.deployOperator();
        } catch (IOException e) {
            log.error("Could not deploy operator into openshift", e);
            fail("Could not deploy operator into openshift" + e.getMessage());
        }

        OpenShiftUtils.getInstance().serviceAccounts()
            .inNamespace(TestConfiguration.openShiftNamespace())
            .withName("apicurito")
            .edit()
            .addNewImagePullSecret("apicurito-pullsecret")
            .done();

        //Delete operator source and clean quay project.
        ocpService.deleteOpsrcToken();

        try {
            ocpService.deleteOperatorSource();
            quayService.deleteQuayProject();
        } catch (IOException e) {
            fail("Fail during cleanup of quay project" +  e.getMessage());
        }
    }

    @When("patch ClusterServiceVersion with UI image {string}")
    public void patchClusterServiceVersionWithDifferentUIImage(String uiImage) {
        log.info("Patching CSV with UI image: " + uiImage);

        String csv = OpenShiftUtils.getInstance().apps().deployments().inNamespace(TestConfiguration.openShiftNamespace()).list().getItems().get(0).getMetadata().getOwnerReferences().get(0).getName();
        final String output = OpenShiftUtils.binary().execute(
            "patch", "ClusterServiceVersion", csv, "--type=json",
            "-p=[" +
                "  {\n" +
                "    \"op\": \"replace\",\n" +
                "    \"path\": \"/spec/install/spec/deployments/0/spec/template/spec/containers/0/env/2\",\n" +
                "    \"value\": {\n" +
                "      \"name\": \"RELATED_IMAGE_APICURITO\",\n" +
                "      \"value\":" + uiImage + "\n" +
                "    }\n" +
                "  }\n" +
                "]"
        );
    }

    @When("delete running instance of apicurito")
    public void deleteRunningInstanceOfApicurito() {
        ApicuritoTemplate.cleanNamespace();
    }

    @Then("clean openshift after operatorhub test")
    public void cleanOpenshiftAfterOperatorhubTest() {
        String csv = OpenShiftUtils.getInstance().apps().deployments().inNamespace(TestConfiguration.openShiftNamespace()).list().getItems().get(0).getMetadata().getOwnerReferences().get(0).getName();
        OpenShiftUtils.binary().execute("delete", "csv", csv);
        OpenShiftUtils.binary().execute("delete", "subscription", "fuse-apicurito");
        ApicuritoTemplate.cleanNamespace();

        String available = "src/test/resources/operatorhubFiles/availableOH.yaml";
        ConfigurationOCPUtils.applyInOCP("Available operators", "openshift-marketplace", available);
    }

    @Then("reinstall apicurito")
    public void reinstallApicurito() {
        ApicuritoTemplate.cleanNamespace();
        ApicuritoTemplate.reinstallApicurito();
    }
}
