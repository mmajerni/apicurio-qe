package apicurito.tests.steps;

import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.ImportExportUtils;
import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;


import java.io.File;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
@Slf4j
public class CommonSteps {

    @Given("^log into apicurito$")
    public void login() {
        Selenide.open(TestConfiguration.apicuritoUrl());
    }

    @When("^create a new API$")
    public void createANewApi() {
        CommonUtils.getButtonWithText("New API", CommonUtils.getAppRoot()).shouldBe(visible, enabled).shouldNotHave(attribute("disabled"))
                .click();
    }

    @And("^sleep for (\\d+) seconds$")
    public void sleepFor(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^import API \"([^\"]*)\"$")
    public void importAPI(String pathtoFile) throws InterruptedException{
        ImportExportUtils.importAPI(new File(pathtoFile));

        //give jenkins more time so the integration shows up in the list //TODO
        //TestUtils.sleepIgnoreInterrupt(TestConfiguration.getJenkinsDelay());
    }
}
