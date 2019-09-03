package apicurito.tests.hooks;

import apicurito.tests.configuration.TestConfiguration;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assume;

@Slf4j
public class CommonHooks {
    //@After("@scenario1,@scenario2")
    @After
    public void oneHook() {
        TestConfiguration.printDivider("Executing afterhook which does nothing so far");
    }

    @Before("@skip_scenario_template")
    public void skip_scenario_template(Scenario scenario) {
        if (!TestConfiguration.useOperator()) {
            TestConfiguration.printDivider("SKIP SCENARIO: " + scenario.getName());
            Assume.assumeTrue(false);
        }
    }

    @Before("@skip_scenario")
    public void skip_scenario(Scenario scenario) {
        TestConfiguration.printDivider("SKIP SCENARIO: " + scenario.getName());
    }
}
