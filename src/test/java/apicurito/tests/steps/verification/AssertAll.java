package apicurito.tests.steps.verification;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AssertAll {

    @When("initialize collector")
    public void initColletor() {
        CollectorHelper.initCollector();
    }

    @Then("^check all for errors$")
    public void checkAllForErrors() {
        CollectorHelper.getCollector().assertAll();
    }
}
