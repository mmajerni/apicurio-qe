package apicurito.tests.steps;

import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.utils.slenide.OurSelenide;
import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;

@Slf4j
public class CommonSteps {

    @Given("^log into apicurito$")
    public void login() {
        Selenide.open(TestConfiguration.apicuritoUrl());
        //String currentUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
    }

    @When("^click on the \"([^\"]*)\" button$")
    public void clickOnButton(String buttonTitle) {
        OurSelenide.getButtonWithTitle(buttonTitle, OurSelenide.getAppRoot()).shouldBe(visible, enabled).shouldNotHave(attribute("disabled")).click();
    }

    @And("^sleep for (\\d+) seconds$")
    public void sleepFor(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
