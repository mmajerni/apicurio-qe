package apicurito.tests.hooks;

import apicurito.tests.configuration.TestConfiguration;
import cucumber.api.java.After;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonHooks {
    //@After("@scenario1,@scenario2")
    @After
    public void oneHook() {
        TestConfiguration.printDivider("Executing afterhook which does nothing so far");
    }
}
