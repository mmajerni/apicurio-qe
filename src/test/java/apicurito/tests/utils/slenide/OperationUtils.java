package apicurito.tests.utils.slenide;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperationUtils {

    private static class OperationElements {
        private static By PATH_PARAMETERS_ROW = By.cssSelector("path-param-row");
        private static By RESPONSES_SECTION = By.cssSelector("responses-section");
    }

    private static class OperationSelenideElements {
        private static SelenideElement DROPDOWN_ELEMENT = getOperationRoot().$("#addResponseModal").$(By.className("dropdown"));
    }

    public static SelenideElement getOperationRoot() {
        return $("operations-section").shouldBe(visible);
    }

    public static void setResponseStatusCode(Integer code) {
        log.info("Setting status code to {}", code);

        OperationSelenideElements.DROPDOWN_ELEMENT.$("#statusCodeDropDown").click();
        OperationSelenideElements.DROPDOWN_ELEMENT.$(By.className("dropdown-menu")).$$("a").filter(text(code.toString())).first().click();

        CommonUtils.getButtonWithText("Add", getOperationRoot()).click();
    }

    public static void selectResponse(Integer code) {
        log.info("Selecting response {}", code);
        getOperationRoot().$(OperationElements.RESPONSES_SECTION).$$(By.className("statusCode")).filter(text(code.toString())).first().click();
    }

    public static void overrideParameter(String parameter) {
        log.info("Overriding parameter {}", parameter);

        SelenideElement param = getOperationRoot().$$(OperationElements.PATH_PARAMETERS_ROW).filter(text(parameter)).first();
        CommonUtils.getButtonWithText("Override", param).click();
    }

    public static void deleteOperation() {
        getOperationRoot().$(By.className("actions")).shouldBe(visible).click();
        MainPageUtils.getDropdownMenuItem("Delete Operation").shouldBe(visible).click();
    }
}
