package apicurito.tests.steps.verification;

import static org.assertj.core.api.Assertions.assertThat;

import static com.codeborne.selenide.Condition.matchText;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.PathUtils;
import cucumber.api.java.en.Then;

public class PathVerifications {

    private static class PathElements {
        private static By PATH_PARAMETERS_SECTION = By.cssSelector("path-params-section");
        private static By PATH_PARAMETERS_ROW = By.cssSelector("path-param-row");
    }

    @Then("^check that operation \"([^\"]*)\" is created for path \"([^\"]*)\"$")       //NEW
    public void checkThatOperationIsCreatedForPath(String operation, String path) {
        SelenideElement pathElement = MainPageUtils.getPathWithName(path);      //TODO private method to check that path is created
        if (pathElement == null) {
            CollectorHelper.getCollector().fail("Operation %s is not created because path %s is not found.", operation, path);
            return;
        } else {
            pathElement.click();
        }

        ElementsCollection ec = PathUtils.getPathPageRoot().$$(By.cssSelector("div." + operation.toLowerCase() + "-tab.enabled"));
        assertThat(ec.size() == 1).as("Operation %s is not created", operation).isTrue();
    }

    @Then("^check that path parameter \"([^\"]*)\" is created for path \"([^\"]*)\"$")
    public void checkThatPathParameterIsCreatedForPath(String parameter, String path) {
        SelenideElement pathElement = MainPageUtils.getPathWithName(path);
        if (pathElement == null) {
            CollectorHelper.getCollector().fail("Parameter %s is not created because path %s is not found.", parameter, path);
            return;
        } else {
            pathElement.click();
        }

        SelenideElement parameterElement = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW).filter(matchText(parameter)).first();
        CollectorHelper.getCollector().assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not created", parameter).doesNotContain("missing");
    }

    @Then("^check that path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(matchText(parameter)).first().$(By.className("description"));

        CollectorHelper.getCollector().assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }

    @Then("^check that path parameter \"([^\"]*)\" has type \"([^\"]*)\" formatted as \"([^\"]*)\"$")
    public void checkThatPathParameterHasTypeFormattedAs(String parameter, String type, String as) {
        SelenideElement summaryElement = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(matchText(parameter)).first().$(By.className("summary"));

        CollectorHelper.getCollector().assertThat(summaryElement.getText()).as("Parameter has not type %s formatted as %s", type, as).isEqualTo(type.toLowerCase() + " as " + as.toLowerCase());
        //TODO need support for array
        //TODO e.g. int32 will not work (different strings in summary)
        //TODO if both type and as are the same ->fail
    }
}
