package apicurito.tests.utils.slenide;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.SelenideElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OperationUtils {

    public static SelenideElement getOperationRoot() {
        return $("operations-section").shouldBe(visible);
    }

    public static void setResponseStatusCode(Integer code) {
        log.info("Setting status code to {}", code);

        getOperationRoot().$("#statusCodeMenu").click();

        getOperationRoot().$$("ul").filter(attribute("class", "dropdown-menu")).first()
                .$$("a").filter(text(code.toString())).first().click();

        CommonUtils.getButtonWithText("Add", getOperationRoot()).click();
    }

    public static void selectResponse(Integer code) {
        log.info("Selecting response {}", code);
        getOperationRoot().$("responses-section").$$(By.className("statusCode")).filter(text(code.toString())).first().click();
    }

    public static void overrideParameter(String parameter) {
        log.info("Overriding parameter {}", parameter);

        SelenideElement param = getOperationRoot().$$("path-param-row").filter(text(parameter)).first();
        CommonUtils.getButtonWithText("Override", param).click();
    }

    public static void deleteOperation() {
        getOperationRoot().$(By.className("actions")).shouldBe(visible).click();
        MainPageUtils.getDropdownMenuItem("Delete Operation").shouldBe(visible).click();
    }
}
