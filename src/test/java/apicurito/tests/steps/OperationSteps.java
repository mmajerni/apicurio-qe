package apicurito.tests.steps;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;


@Slf4j
public class OperationSteps {

    private static SelenideElement SUMMARY_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("summary"));
    private static SelenideElement OPERATION_ID_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("operationId"));
    private static SelenideElement DESCRIPTION_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("description"));
    private static SelenideElement TAGS_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("tags"));
    private static SelenideElement RESPONSE_SECTION = OperationUtils.getOperationRoot().$("responses-section");

    @When("^set summary \"([^\"]*)\"$")
    public void setSummary(String summary){
        CommonUtils.setValueInLabel(summary, SUMMARY_SUBSECTION, false);
    }

    @And("^set operation id \"([^\"]*)\"$")
    public void setOperationId(String operationId){
        CommonUtils.setValueInLabel(operationId, OPERATION_ID_SUBSECTION, false);
    }

    @And("^set description \"([^\"]*)\"$")
    public void setDescription(String description){
        CommonUtils.setValueInTextArea(description, DESCRIPTION_SUBSECTION);
    }

    @And("^set tags \"([^\"]*)\"$")
    public void setTags(String tags) {
        CommonUtils.setValueInLabel(tags, TAGS_SUBSECTION, true);
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
        OperationUtils.selectResponse(response);
        CommonUtils.setValueInTextArea(description, RESPONSE_SECTION);
    }

    @And("^override parameter \"([^\"]*)\"$")
    public void overrideParameter(String parameter) {
        OperationUtils.overrideParameter(parameter);
    }

    @And("^set description \"([^\"]*)\" for override path parameter \"([^\"]*)\" in operation$")
    public void setDescriptionForOverridePathParameterInOperation(String description, String parameter) {
        log.info("Setting description {} for overriden parameter {}", description, parameter);

        SelenideElement parameterElement = OperationUtils.getOperationRoot().$("path-params-section")
                .$$("path-param-row").filter(text(parameter)).first();

        parameterElement.$(By.className("description")).click();

        CommonUtils.setValueInTextArea(description, parameterElement);
    }

    @And("^set response type \"([^\"]*)\" for response (\\d+)$")
    public void setResponseType(String type, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE.getButtonId(), type, RESPONSE_SECTION);
    }

    @And("^set response type of \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeOf(String of, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE_OF.getButtonId(), of, RESPONSE_SECTION);
    }

    @And("^set response type as \"([^\"]*)\" for response (\\d+)$")
    public void setResponseTypeAs(String as, Integer response) {
        OperationUtils.selectResponse(response);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE_AS.getButtonId(), as, RESPONSE_SECTION);
    }
}