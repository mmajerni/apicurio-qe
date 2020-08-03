package apicurito.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.codeborne.selenide.Configuration;

import apicurito.tests.configuration.Component;
import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.configuration.templates.ApicuritoTemplate;
import apicurito.tests.utils.openshift.OpenShiftUtils;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    tags = "not @manual and not @wip and not @ignore",
    plugin = {"pretty", "junit:target/cucumber/cucumber-junit.xml", "json:target/cucumber/cucumber-report.json"})
public class TestRunner {

    @BeforeClass
    public static void beforeTests() {

        if (OpenShiftUtils.xtf().getProject(TestConfiguration.openShiftNamespace()) == null) {
            log.info("Creating new project " + TestConfiguration.openShiftNamespace());
            final String output = OpenShiftUtils.binary().execute(
                    "new-project", TestConfiguration.openShiftNamespace()
            );
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (Boolean.valueOf(TestConfiguration.doReinstall()) && "apicurito".equals(TestConfiguration.openShiftNamespace())) {
            ApicuritoTemplate.cleanNamespace();
            ApicuritoTemplate.setInputStreams();
            ApicuritoTemplate.deploy();
            if (TestConfiguration.useOperator()) {
                ApicuritoTemplate.waitForApicurito("component", 6, Component.SERVICE);
            } else {
                ApicuritoTemplate.waitForApicurito("component", 1, Component.UI);
            }
        }

        //set up Selenide
        Configuration.timeout = TestConfiguration.getConfigTimeout() * 1000;
        //We will now use custom web driver
        //Configuration.browser = TestConfiguration.apicuritoBrowser();
        Configuration.browser = "apicurito.tests.configuration.CustomWebDriverProvider";
        //TODO temporary workaround for jenkins job
        //Configuration.browserSize = "1920x1080";
    }

    @AfterClass
    public static void afterTests() {
        log.info("After Tests");
        if ("operatorhub".equals(TestConfiguration.openShiftNamespace())) {
            ApicuritoTemplate.cleanOcpAfterOperatorhubTest();
        }
        if (TestConfiguration.namespaceCleanupAfter()) {
            TestConfiguration.printDivider("Cleaning namespace after tests");
            OpenShiftUtils.getInstance().clean();
            OpenShiftUtils.getInstance().waiters().isProjectClean().waitFor();
        }
    }
}
