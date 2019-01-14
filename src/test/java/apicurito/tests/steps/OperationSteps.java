package apicurito.tests.steps;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.*;


@Slf4j
public class OperationSteps {

    private static SelenideElement SUMMARY_SUBSECTION = OperationUtils.getOperationRoot().$$("div")
            .filter(attribute("class", "section-field summary")).first();
    private static SelenideElement OPERATION_ID_SUBSECTION = OperationUtils.getOperationRoot().$$("div")
            .filter(attribute("class", "section-field operationId")).first();
    private static SelenideElement DESCRIPTION_ID_SUBSECTION = OperationUtils.getOperationRoot().$$("div")
            .filter(attribute("class", "section-field description")).first();
    private static SelenideElement TAGS_SUBSECTION = OperationUtils.getOperationRoot().$$("div")
            .filter(attribute("class", "section-field tags")).first();

    @When("^set summary \"([^\"]*)\"$")
    public void setSummary(String summary){
        OperationUtils.clickToEditLabel(SUMMARY_SUBSECTION);
        CommonUtils.getLabelWithType("text", SUMMARY_SUBSECTION)
                .setValue(summary);
        CommonUtils.getButtonWithTitle("Save changes.", OperationUtils.getOperationRoot())
                .click();
    }

    @And("^set operation id \"([^\"]*)\"$")
    public void setOperationId(String operationId){
        OperationUtils.clickToEditLabel(OPERATION_ID_SUBSECTION);
        CommonUtils.getLabelWithType("text", OPERATION_ID_SUBSECTION)
                .setValue(operationId);
        CommonUtils.getButtonWithTitle("Save changes.", OperationUtils.getOperationRoot())
                .click();
    }

    @And("^set description \"([^\"]*)\"$")
    public void setDescription(String description){
        OperationUtils.clickToEditDescriptionTextArea(DESCRIPTION_ID_SUBSECTION);
        OperationUtils.getOperationRoot().$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", OperationUtils.getOperationRoot())
                .click();
    }

    @And("^set tags \"([^\"]*)\"$")
    public void setTags(String tags)  {
        OperationUtils.clickToEditTagsLabel(TAGS_SUBSECTION);
        CommonUtils.getLabelWithType("text", TAGS_SUBSECTION)
                .setValue(tags);
        CommonUtils.getButtonWithTitle("Save changes.", OperationUtils.getOperationRoot())
                .click();
    }

    @And("^set response (\\d+) with clickable link$")
    public void setResponseWithLink(Integer response){
        CommonUtils.getClickableLink(CommonUtils.Sections.RESPONSE, CommonUtils.getAppRoot().shouldBe(visible,enabled).shouldNotHave(attribute("disabled")))
                .click();
        OperationUtils.setResponseStatusCode(response);
    }

    @And("^set response (\\d+) with plus sign$")
    public void setResponseWithPlusSign(Integer response){
        CommonUtils.getNewPlusSignButton(CommonUtils.Sections.RESPONSE, CommonUtils.getAppRoot().shouldBe(visible,enabled).shouldNotHave(attribute("disabled")))
                .click();
        OperationUtils.setResponseStatusCode(response);
    }

    @And("^set response description \"([^\"]*)\" for response (\\d+)$")
    public void setDescriptionForResponse(String description, Integer response){
        OperationUtils.openResponseDescription(response);
        OperationUtils.clickToEditDescriptionTextArea(OperationUtils.getOperationRoot().$$("div").filter(attribute("class", "response-description")).first());
        OperationUtils.getOperationRoot().$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", OperationUtils.getOperationRoot())
                .click();
    }

    /**
     * TODO support also for array
     */
    @And("^set response type \"([^\"]*)\" formatted as \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeFormattedAs(String type, String as, Integer response){
        OperationUtils.openResponseTypes(response);
        OperationUtils.setResponseType(type);
        OperationUtils.setFormattedAs(as);
    }

    @And("^override parameter \"([^\"]*)\"$")
    public void overrideParameter(String parameter) {
        OperationUtils.overrideParameter(parameter);
    }

    @And("^set description \"([^\"]*)\" for override path parameter \"([^\"]*)\" in operation$")
    public void setDescriptionForOverridePathParameterInOperation(String description, String parameter) {
        OperationUtils.setDescriptionForOverrideParameter(description,parameter);
    }
}