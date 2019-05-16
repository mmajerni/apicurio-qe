package apicurito.tests.steps;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideElement;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.PathUtils;
import cucumber.api.java.en.When;

public class PathSteps {

    private static SelenideElement PARAMETERS_SECTION = PathUtils.getPathPageRoot().$("path-params-section");

    /**
     * @param operation must be convertable to operation enum. See Operations enum
     */
    @When("^create new \"([^\"]*)\" operation$")
    public void createNewOperation(String operation) {
        PathUtils.getCreateOperationButton(Operations.valueOf(operation))
                .click();
    }

    @When("^select operation \"([^\"]*)\"$")
    public void selectOperation(String operation) {
        PathUtils.getOperationButton(Operations.valueOf(operation), CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                .click();
    }

    @When("^create path parameter \"([^\"]*)\"$")
    public void createPathParameter(String parameter) {
        PathUtils.createPathParameter(parameter);
    }

    @When("^set description \"([^\"]*)\" for path parameter \"([^\"]*)\"$")
    public void setDescriptionPathParameter(String description, String parameter) {
        PathUtils.openPathDescription(parameter);
        CommonUtils.setValueInTextArea(description, PARAMETERS_SECTION);
    }

    @When("^set path parameter type \"([^\"]*)\" for path parameter \"([^\"]*)\"$")
    public void setPathParameterTypeForPathParameter(String type, String parameter) {
        PathUtils.openPathTypes(parameter);
        SelenideElement parameterElement = PARAMETERS_SECTION.$$("path-param-row")
                .filter(text(parameter)).first();

        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(), type, parameterElement);
    }

    @When("^set path parameter type of \"([^\"]*)\" for path parameter \"([^\"]*)\"$")
    public void setPathParameterTypeOfForPathParameter(String of, String parameter) {
        PathUtils.openPathTypes(parameter);
        SelenideElement parameterElement = PARAMETERS_SECTION.$$("path-param-row")
                .filter(text(parameter)).first();

        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(), of, parameterElement);
    }

    @When("^set path parameter type as \"([^\"]*)\" for path parameter \"([^\"]*)\"$")
    public void setPathParameterTypeAsForPathParameter(String as, String parameter) {
        PathUtils.openPathTypes(parameter);
        SelenideElement parameterElement = PARAMETERS_SECTION.$$("path-param-row")
                .filter(text(parameter)).first();

        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(), as, parameterElement);
    }

    public enum Operations {
        GET,
        PUT,
        POST,
        DELETE,
        OPTIONS,
        HEAD,
        PATCH,
        TRACE
    }
}
