package apicurito.tests.utils.slenide;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class OperationUtils {

    public static SelenideElement getOperationRoot() { return $(By.tagName("operation-form")).shouldBe(visible); }

    public static void clickToEditLabel(SelenideElement section){
        section.$$("span").filter(attribute("title", "Click to edit.")).first().click();
    }

    public static void clickToEditDescriptionTextArea(SelenideElement section){
        section.$$("div").filter(attribute("title", "Click to edit.")).first().click();
    }

    public static void clickToEditTagsLabel(SelenideElement section){
        section.$$("i").filter(attribute("title", "Click to edit.")).first().click();
    }

    public static void setResponseStatusCode(Integer code){
        getOperationRoot().$("#statusCodeMenu").click();

        getOperationRoot().$$("ul").filter(attribute("class", "dropdown-menu")).first()
                .$$("a").filter(text(code.toString())).first().click();

        CommonUtils.getButtonWithText("Add", getOperationRoot()).click();

    }

    public static void openResponseDescription(Integer code){
        getOperationRoot().$$("response-row")
                .filter(text(code.toString())).first()
                .$$("div").filter(attribute("class", "description"))
                .first()
                .click();
    }

    public static void openResponseTypes(Integer code){
        getOperationRoot().$$("response-row")
                .filter(text(code.toString())).first()
                .$$("div").filter(attribute("class", "summary"))
                .first()
                .click();
    }

    public static void setResponseType(String type){
        SelenideElement response = getOperationRoot().$("#api-response-type");
        response.click();
        response.parent().$$("li").filter(text(type)).first().click();
    }

    public static void setFormattedAs(String as){
        SelenideElement response = getOperationRoot().$("#api-response-type-as");
        response.click();
        response.parent().$$("li").filter(text(as)).first().click();
    }

    public static void overrideParameter(String parameter){
        SelenideElement param = getOperationRoot().$$("path-param-row").filter(text(parameter)).first();
        CommonUtils.getButtonWithText("Override",param).click();
    }

    public static void setDescriptionForOverrideParameter(String description, String parameter){
        SelenideElement parameterElement = getOperationRoot().$("#path-parameters-section-body")
                .$$("path-param-row").filter(text(parameter)).first();

        parameterElement.$$("div").filter(attribute("class", "description")).first().click();
        parameterElement.$$("div").filter(attribute("title","Click to edit.")).first().click();

        parameterElement.$("ace-editor textarea").sendKeys(Keys.CONTROL + "a");
        parameterElement.$("ace-editor textarea").sendKeys(Keys.DELETE);
        parameterElement.$("ace-editor textarea").sendKeys(description);

        CommonUtils.getButtonWithTitle("Save changes.", parameterElement)
                .click();
    }

}
