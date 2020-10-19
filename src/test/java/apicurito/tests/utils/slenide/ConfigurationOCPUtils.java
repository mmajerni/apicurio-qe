package apicurito.tests.utils.slenide;

import static org.assertj.core.api.Assertions.fail;

import org.yaml.snakeyaml.Yaml;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.utils.HttpUtils;
import apicurito.tests.utils.openshift.OpenShiftUtils;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigurationOCPUtils {

    public static void waitForOperatorUpdate() {
        log.info("Waiting for 2 created replica sets");
        //Wait for 2 minutes for replica sets
        if (!areTwoReplicaSetsAvailable()) {
            fail("TIMEOUT 2 minutes : Failed to load 2 replica sets");
        }

        log.info("Waiting for exactly 3 running Apicurito UI pods.");
        //Wait 3 minutes for 3 running pods
        if (!areExactlyThreeUiPodsRunning()) {
            fail("TIMEOUT 3 minutes: Failed to load 3 running Apicurito pods");
        }
    }

    public static void waitForRollout() {
        //Wait for Rollout until there is no unavailable pod
        log.info("Waiting for pods rollout.");
        Integer tmp = Integer.MAX_VALUE;
        while (tmp != null) {
            //Wait for 5 seconds
            CommonUtils.sleepFor(5);

            tmp = OpenShiftUtils.getInstance().apps().deployments().inNamespace(TestConfiguration.openShiftNamespace()).list().getItems().get(1)
                .getStatus().getUnavailableReplicas();
        }

        //Wait another 15 seconds because of termination running pods
        CommonUtils.sleepFor(15);
    }

    public static void setTestEnvToOperator(String nameOfEnv, String valueOfEnv) {
        log.info("Setting test ENV: " + nameOfEnv + "=" + valueOfEnv);
        final String output = OpenShiftUtils.binary().execute(
            "set",
            "env",
            "deployment",
            "fuse-apicurito",
            nameOfEnv + "=" + valueOfEnv
        );
    }

    public static void createInOCP(String itemName, String item) {
        log.info("Creating " + itemName + " from: " + item);

        final String output = OpenShiftUtils.binary().execute(
            "create",
            "-n", TestConfiguration.openShiftNamespace(),
            "-f", item
        );
    }

    public static void applyInOCP(String itemName, String item) {
        log.info("Applying {} from: {}", itemName, item);
        final String output = OpenShiftUtils.binary().execute(
            "apply", "-n", TestConfiguration.openShiftNamespace(), "-f", item
        );
    }

    public static void applyInOCP(String itemName, String namespace, String item) {
        log.info("Applying {} from: {}", itemName, item);
        final String output = OpenShiftUtils.binary().execute(
            "apply", "-n", namespace, "-f", item
        );
    }

    public static String getOperatorImage() {
        log.info("Getting operator image from operator deployment file");
        try {
            String deploymentConfig = HttpUtils.readFileFromURL(new URL(TestConfiguration.apicuritoOperatorDeploymentUrl()));
            Map<String, Object> deployment = new Yaml().load(deploymentConfig);
            return ((Map<String, String>) ((Map<String, List<Map>>) ((Map<String, Map>) deployment.get("spec")).get("template").get("spec"))
                .get("containers").get(0)).get("image");
        } catch (MalformedURLException e) {
            log.error("Proper URL was not supplied", e);
            return null;
        }
    }

    private static List<ReplicaSet> getApicuritoUIreplicaSets() {
        List<ReplicaSet> uiRs = new ArrayList<>();

        List<ReplicaSet> listRs = OpenShiftUtils.getInstance().apps().replicaSets().list().getItems();
        for (ReplicaSet rs : listRs) {
            if ("apicurito-service-ui".equals(rs.getMetadata().getOwnerReferences().get(0).getName())) {
                uiRs.add(rs);
            }
        }
        return uiRs;
    }

    private static boolean areTwoReplicaSetsAvailable() {
        int counter = 0;
        while (counter < 12) {
            CommonUtils.sleepFor(10);

            if (getApicuritoUIreplicaSets().size() == 2) {
                return true;
            }
            ++counter;
        }
        return false;
    }

    private static boolean areExactlyThreeUiPodsRunning() {
        List<ReplicaSet> uiRs = new ArrayList<>();
        int counter = 0;

        while (counter < 18) {
            uiRs.clear();
            uiRs = getApicuritoUIreplicaSets();
            CommonUtils.sleepFor(10);

            if (uiRs.get(0).getStatus().getReplicas() + uiRs.get(1).getStatus().getReplicas() == 3) {
                return true;
            }
            ++counter;
        }
        return false;
    }
}
