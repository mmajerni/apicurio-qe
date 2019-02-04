package apicurito.tests.steps;

import apicurito.tests.utils.slenide.*;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.And;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;

public class PathSteps {

    private static SelenideElement PARAMETERS_SECTION = PathUtils.getPathPageRoot().$("path-params-section");

    /**
     *
     * @param operation must be convertable to operation enum. See Operations enum
     */
    @And("^create new \"([^\"]*)\" operation$")
    public void createNewOperation(String operation){
        PathUtils.getCreateOperationButton(Operations.valueOf(operation), CommonUtils.getAppRoot().shouldBe(visible,enabled).shouldNotHave(attribute("disabled")))
                .click();
    }

    @And("^update operation \"([^\"]*)\"$")
    public void updateOperation(String operation){
        PathUtils.getOperationButton(Operations.valueOf(operation), CommonUtils.getAppRoot().shouldBe(visible,enabled).shouldNotHave(attribute("disabled")))
                .click();
    }

    @And("^create path parameter \"([^\"]*)\"$")
    public void createPathParameter(String parameter) {
        PathUtils.createPathParameter(parameter);
    }

    @And("^set description \"([^\"]*)\" for path parameter \"([^\"]*)\"$")
    public void setDescriptionPathParameter(String description, String parameter) {
        PathUtils.openPathDescription(parameter);
        OperationUtils.clickToEditDescriptionTextArea(PARAMETERS_SECTION);
        PARAMETERS_SECTION.$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", PARAMETERS_SECTION)
                .click();
    }

    @And("^set type \"([^\"]*)\" formatted as \"([^\"]*)\" for parameter \"([^\"]*)\"$")
    public void setTypeFormattedAsForParameter(String type, String as, String parameter) {
        PathUtils.openPathTypes(parameter);
        PathUtils.setPathType(parameter, type);
        PathUtils.setFormattedAs(parameter, as);
    }

    public enum Operations{
        GET,
        PUT,
        POST,
        DELETE,
        OPTIONS,
        HEAD,
        PATCH
    }
}
