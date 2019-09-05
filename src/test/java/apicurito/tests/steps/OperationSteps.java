package apicurito.tests.steps;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import apicurito.tests.utils.slenide.PathUtils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;

@Slf4j
public class OperationSteps {

    private static class OperationElements {
        private static By DESCRIPTION = By.className("description");
        private static By CONSUMES = By.className("consumes");
        private static By PRODUCES = By.className("produces");
        private static By A = By.cssSelector("a");

        private static By RESPONSE_SECTION = By.cssSelector("responses-section");
        private static By REQUEST_BODY_SECTION = By.cssSelector("requestbody-section");
        private static By PATH_PARAMETERS_SECTION = By.cssSelector("path-params-section");
        private static By REQUIREMENTS_SECTION = By.cssSelector("security-requirements-section");
    }

    @When("^set operation summary \"([^\"]*)\"$")
    public void setSummary(String summary) {
        CommonUtils.setValueInLabel(summary, OperationUtils.getOperationRoot().$(By.className("summary")), false);
    }

    @When("^set operation id \"([^\"]*)\"$")
    public void setOperationId(String operationId) {
        CommonUtils.setValueInLabel(operationId, OperationUtils.getOperationRoot().$(By.className("operationId")), false);
    }

    @When("^set operation description \"([^\"]*)\"$")
    public void setDescription(String description) {
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(OperationElements.DESCRIPTION));
    }

    @When("^set operation tags \"([^\"]*)\"$")
    public void setTags(String tags) {
        CommonUtils.setValueInLabel(tags, OperationUtils.getOperationRoot().$(By.className("tags")), true);
    }

    @When("^set response (\\d+) with clickable link$")
    public void setResponseWithLink(Integer response) {
        CommonUtils.getClickableLink(CommonUtils.Sections.RESPONSE, CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                .click();
        OperationUtils.setResponseStatusCode(response);
    }

    @When("^set response (\\d+) with plus sign$")
    public void setResponseWithPlusSign(Integer response) {
        CommonUtils.getNewPlusSignButton(CommonUtils.Sections.RESPONSE, CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                .click();
        OperationUtils.setResponseStatusCode(response);
    }

    @When("^set response description \"([^\"]*)\" for response (\\d+)$")
    public void setDescriptionForResponse(String description, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(OperationElements.RESPONSE_SECTION));
    }

    @When("^override parameter \"([^\"]*)\"$")
    public void overrideParameter(String parameter) {
        OperationUtils.overrideParameter(parameter);
    }

    @When("^set description \"([^\"]*)\" for override path parameter \"([^\"]*)\" in operation$")
    public void setDescriptionForOverridePathParameterInOperation(String description, String parameter) {
        log.info("Setting description {} for overriden parameter {}", description, parameter);

        SelenideElement parameterElement = OperationUtils.getOperationRoot().$(OperationElements.PATH_PARAMETERS_SECTION)
                .$$("path-param-row").filter(text(parameter)).first();

        parameterElement.$(OperationElements.DESCRIPTION).click();

        CommonUtils.setValueInTextArea(description, parameterElement);
    }

    @When("^set response type \"([^\"]*)\" for response (\\d+)$")
    public void setResponseType(String type, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(), type, OperationUtils.getOperationRoot().$(OperationElements.RESPONSE_SECTION));
    }

    @When("^set response type of \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeOf(String of, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(), of, OperationUtils.getOperationRoot().$(OperationElements.RESPONSE_SECTION));
    }

    @When("^set response type as \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeAs(String as, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(), as, OperationUtils.getOperationRoot().$(OperationElements.RESPONSE_SECTION));
    }

    @When("^override consumes with \"([^\"]*)\" for operation \"([^\"]*)\"$")
    public void overrideConsumesWithForOperation(String consumes, String operation) {
        SelenideElement consumesSubsection = OperationUtils.getOperationRoot().$(OperationElements.CONSUMES);

        PathUtils.getOperationButton(PathSteps.Operations.valueOf(operation), OperationUtils.getOperationRoot())
                .click();

        CommonUtils.getButtonWithText("Override", consumesSubsection)
                .click();

        CommonUtils.getLabelWithType("text", consumesSubsection).setValue(consumes);
        CommonUtils.getButtonWithTitle("Save changes.", consumesSubsection).click();
    }

    @When("^override produces with \"([^\"]*)\" for operation \"([^\"]*)\"$")
    public void overrideProducesWithForOperation(String produces, String operation) {
        SelenideElement producesSubsection = OperationUtils.getOperationRoot().$(OperationElements.PRODUCES);

        PathUtils.getOperationButton(PathSteps.Operations.valueOf(operation), OperationUtils.getOperationRoot())
                .click();

        CommonUtils.getButtonWithText("Override", OperationUtils.getOperationRoot().$(OperationElements.PRODUCES))
                .click();
        CommonUtils.getLabelWithType("text", producesSubsection).setValue(produces);
        CommonUtils.getButtonWithTitle("Save changes.", producesSubsection).click();
    }

    @When("^delete current operation$")
    public void deleteOperation() {
        OperationUtils.deleteOperation();       //TODO specify operation
    }

    @When("^create request body$")
    public void createRequestBody() {
        ElementsCollection collapsedSection = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$$(OperationElements.A).filter(attribute("class", "collapsed"));
        if (collapsedSection.size() > 0) {
            collapsedSection.first().click();
        }
        OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$$(OperationElements.A)
                .filter(text("Add a request body")).shouldHaveSize(1).first()   //TODO refactor with enum Sections
                .shouldBe(visible).click();
    }

    @When("^set request body description \"([^\"]*)\"$")
    public void setRequestBodyDescription(String description) {
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION));
    }

    @When("^set request body type \"([^\"]*)\"$")
    public void setRequestBodyType(String type) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(),
                type, OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION));
    }

    @When("^set request body type of \"([^\"]*)\"$")
    public void setRequestBodyTypeOf(String of) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(),
                of, OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION));
    }

    @When("^set request body type as \"([^\"]*)\"$")
    public void setRequestBodyTypeAs(String as) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(),
                as, OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION));
    }

    @When("^override security requirements in operation with$")
    public void overrideSecurityRequirementsInOperationWith(DataTable table) {      //TODO same method as in MainPageSteps --> join
        CommonUtils.getNewPlusSignButton(CommonUtils.Sections.REQUIREMENT, MainPageUtils.getMainPageRoot().$(OperationElements.REQUIREMENTS_SECTION))
                .click();
        SelenideElement requirementEditor = MainPageUtils.getMainPageRoot().$("security-requirement-editor");

        for (List<String> dataRow : table.cells()) {
            ElementsCollection listOfSchemes = requirementEditor.$$(By.className("list-group-item"));
            for (SelenideElement scheme : listOfSchemes) {
                if (scheme.$(By.className("name")).getText().equals(dataRow.get(0)) && !scheme.getAttribute("class").contains("active")) {
                    scheme.$("input").click();
                    break;
                }
            }
        }
        CommonUtils.getButtonWithText("Save", requirementEditor)
                .click();
    }
}
