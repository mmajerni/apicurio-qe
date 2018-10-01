package apicurito.tests.utils.slenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


@Slf4j
public class OurSelenide {

    public static SelenideElement getAppRoot() {
        return $(By.tagName("app-root")).shouldBe(visible);
    }

    public static SelenideElement getButtonWithTitle(String buttonTitle, SelenideElement differentRoot) {
        log.info("searching for button {}", buttonTitle);

        return differentRoot.shouldBe(visible).findAll(By.tagName("button"))
                .filter(Condition.matchText("(\\s*)" + buttonTitle + "(\\s*)")).shouldHave(sizeGreaterThanOrEqual(1)).first();
    }
}
