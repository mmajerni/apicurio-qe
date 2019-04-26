package apicurito.tests.steps;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import apicurito.tests.utils.slenide.PathUtils;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperationSteps {

    private static By SUMMARY_SUBSECTION = By.className("summary");
    private static By OPERATION_ID_SUBSECTION = By.className("operationId");
    private static By DESCRIPTION_SUBSECTION = By.className("description");
    private static By TAGS_SUBSECTION = By.className("tags");
    private static By CONSUMES_SUBSECTION = By.className("consumes");
    private static By PRODUCES_SUBSECTION = By.className("produces");

    private static By RESPONSE_SECTION = By.cssSelector("responses-section");
    public static By REQUEST_BODY_SECTION = By.cssSelector("requestbody-section");
    public static By QUERY_PARAM_SECTION = By.cssSelector("query-params-section");
    public static By HEADER_PARAM_SECTION = By.cssSelector("header-params-section");

    @When("^set operation summary \"([^\"]*)\"$")
    public void setSummary(String summary) {
        CommonUtils.setValueInLabel(summary, OperationUtils.getOperationRoot().$(SUMMARY_SUBSECTION), false);
    }

    @When("^set operation id \"([^\"]*)\"$")
    public void setOperationId(String operationId) {
        CommonUtils.setValueInLabel(operationId, OperationUtils.getOperationRoot().$(OPERATION_ID_SUBSECTION), false);
    }

    @When("^set operation description \"([^\"]*)\"$")
    public void setDescription(String description) {
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(DESCRIPTION_SUBSECTION));
    }

    @When("^set operation tags \"([^\"]*)\"$")
    public void setTags(String tags) {
        CommonUtils.setValueInLabel(tags, OperationUtils.getOperationRoot().$(TAGS_SUBSECTION), true);
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
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(RESPONSE_SECTION));
    }

    @When("^override parameter \"([^\"]*)\"$")
    public void overrideParameter(String parameter) {
        OperationUtils.overrideParameter(parameter);
    }

    @When("^set description \"([^\"]*)\" for override path parameter \"([^\"]*)\" in operation$")
    public void setDescriptionForOverridePathParameterInOperation(String description, String parameter) {
        log.info("Setting description {} for overriden parameter {}", description, parameter);

        SelenideElement parameterElement = OperationUtils.getOperationRoot().$("path-params-section")
                .$$("path-param-row").filter(text(parameter)).first();

        parameterElement.$(By.className("description")).click();

        CommonUtils.setValueInTextArea(description, parameterElement);
    }

    @When("^set response type \"([^\"]*)\" for response (\\d+)$")
    public void setResponseType(String type, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(), type, OperationUtils.getOperationRoot().$(RESPONSE_SECTION));
    }

    @When("^set response type of \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeOf(String of, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(), of, OperationUtils.getOperationRoot().$(RESPONSE_SECTION));
    }

    @When("^set response type as \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeAs(String as, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(), as, OperationUtils.getOperationRoot().$(RESPONSE_SECTION));
    }

    @When("^override consumes with \"([^\"]*)\" for operation \"([^\"]*)\"$")
    public void overrideConsumesWithForOperation(String consumes, String operation) {
        PathUtils.getOperationButton(PathSteps.Operations.valueOf(operation), OperationUtils.getOperationRoot())
                .click();

        CommonUtils.getButtonWithText("Override", OperationUtils.getOperationRoot().$(CONSUMES_SUBSECTION))
                .click();
        CommonUtils.setValueInLabel(consumes, OperationUtils.getOperationRoot().$(CONSUMES_SUBSECTION), true);
    }

    @When("^override produces with \"([^\"]*)\" for operation \"([^\"]*)\"$")
    public void overrideProducesWithForOperation(String produces, String operation) {
        PathUtils.getOperationButton(PathSteps.Operations.valueOf(operation), OperationUtils.getOperationRoot())
                .click();

        CommonUtils.getButtonWithText("Override", OperationUtils.getOperationRoot().$(PRODUCES_SUBSECTION))
                .click();
        CommonUtils.setValueInLabel(produces, OperationUtils.getOperationRoot().$(PRODUCES_SUBSECTION), true);
    }

    @When("^delete current operation$")
    public void deleteOperation() {
        OperationUtils.deleteOperation();       //TODO specify operation
    }

    @When("^create request body$")
    public void createRequestBody() {
        ElementsCollection collapsedSection = OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION).$$("a").filter(attribute("class", "collapsed"));
        if (collapsedSection.size() > 0) {
            collapsedSection.first().click();
        }
        OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION).$$("a")
                .filter(text("Add a request body")).shouldHaveSize(1).first()   //TODO refactor with enum Sections
                .shouldBe(visible).click();
    }

    @When("^set request body description \"([^\"]*)\"$")
    public void setRequestBodyDescription(String description) {
        CommonUtils.setValueInTextArea(description, OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION));
    }

    @When("^set request body type \"([^\"]*)\"$")
    public void setRequestBodyType(String type) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(),
                type, OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION));
    }

    @When("^set request body type of \"([^\"]*)\"$")
    public void setRequestBodyTypeOf(String of) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(),
                of, OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION));
    }

    @When("^set request body type as \"([^\"]*)\"$")
    public void setRequestBodyTypeAs(String as) {
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(),
                as, OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION));
    }

    @When("^create request form data$")
    public void createRequestFormData(DataTable table) {
        CommonUtils.openCollapsedSection(REQUEST_BODY_SECTION);

        OperationUtils.getOperationRoot().$(REQUEST_BODY_SECTION).$$("a")
                .filter(text("add request form data")).shouldHaveSize(1).first()   //TODO refactor with enum Sections
                .shouldBe(visible).click();                                        //TODO support for more than one form

        CommonUtils.fillEntityEditorForm(table);
    }

    @When("^create query parameters$")
    public void addQueryParameters(DataTable table) {
        CommonUtils.openCollapsedSection(QUERY_PARAM_SECTION);

        OperationUtils.getOperationRoot().$(QUERY_PARAM_SECTION).$$("a")
                .filter(text("add a query parameter")).shouldHaveSize(1).first()   //TODO refactor with enum Sections
                .shouldBe(visible).click();

        CommonUtils.fillEntityEditorForm(table);
    }

    @When("^create header parameters$")
    public void createHeaderParameters(DataTable table) {
        CommonUtils.openCollapsedSection(HEADER_PARAM_SECTION);

        OperationUtils.getOperationRoot().$(HEADER_PARAM_SECTION).$$("a")
                .filter(text("add a header parameter")).shouldHaveSize(1).first()   //TODO refactor with enum Sections
                .shouldBe(visible).click();

        CommonUtils.fillEntityEditorForm(table);
    }

    @When("^override security requirements in operation with$")
    public void overrideSecurityRequirementsInOperationWith(DataTable table) {      //TODO same method as in MainPageSteps --> join
        CommonUtils.getNewPlusSignButton(CommonUtils.Sections.REQUIREMENT, OperationUtils.getOperationRoot().$(MainPageUtils.REQUIREMENTS_SECTION))
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