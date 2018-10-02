package apicurito.tests.configuration.templates;

import apicurito.tests.configuration.Component;
import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.utils.openshift.OpenShiftUtils;
import io.fabric8.kubernetes.api.model.CronJob;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesList;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.openshift.api.model.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.fail;

@Slf4j
public class ApicuritoTemplate {

    public static Template getTemplate() {
        try (InputStream is = new URL(TestConfiguration.templateUrl()).openStream()) {
            return OpenShiftUtils.client().templates().load(is).get();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read apicurito template ", ex);
        }
    }

    public static KubernetesList getImageStreamList() {
        try (InputStream is = new URL(TestConfiguration.templateInputStreamUrl()).openStream()) {
            return OpenShiftUtils.client().lists().load(is).get();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read input image stream list", ex);
        }
    }

    public static void setInputStreams() {
        TestConfiguration.printDivider("Setting up input streams");
        OpenShiftUtils.getInstance().createResources(getImageStreamList());
    }

    public static void deployUsingTemplate() {
        TestConfiguration.printDivider("Deploying using template");

        // get the template
        Template template = getTemplate();
        // set params
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("ROUTE_HOSTNAME", TestConfiguration.openShiftNamespace() + "." + TestConfiguration.openShiftRouteSuffix());
        log.info("Deploying on address: https://" + TestConfiguration.openShiftNamespace() + "." + TestConfiguration.openShiftRouteSuffix());
        templateParams.put("OPENSHIFT_MASTER", TestConfiguration.openShiftUrl());
        templateParams.put("OPENSHIFT_PROJECT", TestConfiguration.openShiftNamespace());
        templateParams.put("IMAGE_STREAM_NAMESPACE", TestConfiguration.openShiftNamespace());

        // process & create
        KubernetesList processedTemplate = OpenShiftUtils.getInstance().recreateAndProcessTemplate(template, templateParams);
        for (HasMetadata hasMetadata : processedTemplate.getItems()) {
            if (!(hasMetadata instanceof CronJob)) {
                OpenShiftUtils.getInstance().createResources(hasMetadata);
            }
        }
    }

    public static void cleanNamespace() {
        TestConfiguration.printDivider("Deleting namespace...");
        try {
            OpenShiftUtils.client().apps().statefulSets().inNamespace(TestConfiguration.openShiftNamespace()).delete();
            OpenShiftUtils.client().extensions().deployments().inNamespace(TestConfiguration.openShiftNamespace()).delete();
            OpenShiftUtils.client().customResourceDefinitions().delete();
        } catch (KubernetesClientException ex) {
            // Probably user does not have permissions to delete.. a nice exception will be printed when deploying
        }
        OpenShiftUtils.getInstance().cleanAndAssert();
        OpenShiftUtils.xtf().getTemplates().forEach(OpenShiftUtils.xtf()::deleteTemplate);
    }

    public static void waitForApicurito() {
        TestConfiguration.printDivider("Waiting for Apicurito to become ready...");

        EnumSet<Component> components = EnumSet.allOf(Component.class);

        ExecutorService executorService = Executors.newFixedThreadPool(components.size());
        components.forEach(c -> {
            Runnable runnable = () ->
                    OpenShiftUtils.xtf().waiters()
                            .areExactlyNPodsReady(1, "component", c.getName())
                            .interval(TimeUnit.SECONDS, 10)
                            .timeout(TimeUnit.MINUTES, 6)
                            .assertEventually();
            executorService.submit(runnable);
        });

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(20, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
                fail("Apicurito wasn't initilized in time");
            }
        } catch (InterruptedException e) {
            fail("Apicurito wasn't initilized in time");
        }
    }
}
