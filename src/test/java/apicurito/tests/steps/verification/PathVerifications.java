package apicurito.tests.steps.verification;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.PathUtils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.Then;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class PathVerifications {

    private static class PathElements {
        private static By PATH_PARAMETERS_SECTION = By.cssSelector("path-params-section");
        private static By PATH_PARAMETERS_ROW = By.cssSelector("path-param-row");
    }

    @Then("^check that operation \"([^\"]*)\" is created for path \"([^\"]*)\"$")
    public void checkThatOperationIsCreatedForPath(String operation, String path) {
        SelenideElement pathElement = MainPageUtils.getPathWithName(path);      //TODO private method to check that path is created
        if (pathElement == null) {
            fail("Operation %s is not created because path %s is not found.", operation, path);
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
            fail("Parameter %s is not created because path %s is not found.", parameter, path);
            return;
        } else {
            pathElement.click();
        }

        SelenideElement parameterElement = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW).filter(matchText(parameter)).first();
        assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not created", parameter).doesNotContain("missing");
    }

    @Then("^check that path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(matchText(parameter)).first().$(By.className("description"));

        assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }

    @Then("^check that path parameter \"([^\"]*)\" has type \"([^\"]*)\"$")
    public void checkThatPathParameterHasType(String parameter, String expectedType) {
        PathUtils.openPathTypes(parameter);
        String type = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(text(parameter)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        assertThat(type).as("Type is %s but should be %s", type, expectedType).isEqualTo(expectedType);
    }

    @Then("^check that path parameter \"([^\"]*)\" has type of \"([^\"]*)\"$")
    public void checkThatPathParameterHasTypeOf(String parameter, String expectedOf) {
        PathUtils.openPathTypes(parameter);
        String of = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(text(parameter)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        assertThat(of).as("Type of is %s but should be %s", of, expectedOf).isEqualTo(expectedOf);
    }

    @Then("^check that path parameter \"([^\"]*)\" has type as \"([^\"]*)\"$")
    public void checkThatPathParameterHasTypeAs(String parameter, String expectedAs) {
        PathUtils.openPathTypes(parameter);
        String as = PathUtils.getPathPageRoot().$(PathElements.PATH_PARAMETERS_SECTION).$$(PathElements.PATH_PARAMETERS_ROW)
                .filter(text(parameter)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        assertThat(as).as("Type as is %s but should be %s", as, expectedAs).isEqualTo(expectedAs);
    }
}
