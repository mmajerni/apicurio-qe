package apicurito.tests.utils.slenide;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class CommonUtils {

    public static SelenideElement getAppRoot() {
        return $(By.tagName("app-root")).shouldBe(visible);
    }

    public static SelenideElement getButtonWithText(String buttonText, SelenideElement differentRoot) {
        log.info("searching for button {}", buttonText);

        return differentRoot.shouldBe(visible).$$("button").filter(text(buttonText))
                .shouldHave(sizeGreaterThanOrEqual(1)).first();
    }

    public static SelenideElement getButtonWithTitle(String buttonTitle, SelenideElement differentRoot) {
        log.info("searching for button with title {}", buttonTitle);

        return differentRoot.shouldBe(visible).$$("button").filter(attribute("title", buttonTitle))
                .shouldHave(sizeGreaterThanOrEqual(1)).first();
    }

    public static SelenideElement getLabelWithName(String labelName, SelenideElement differentRoot) {
        log.info("searching for label {}", labelName);
        return differentRoot.shouldBe(visible).find(By.xpath("//input[contains(@name,\'" + labelName + "\')]"));
    }

    public static SelenideElement getLabelWithType(String labelType, SelenideElement differentRoot) {
        log.info("searching for label {}", labelType);
        return differentRoot.shouldBe(visible).$$("input").filter(attribute("type" , labelType)).first();
    }

    public static SelenideElement getClickableLink(Sections section, SelenideElement differentRoot){
        log.info("searching for link in section {}", section);

        return differentRoot.$$("a").find(text(section.getA()));
    }

    public static SelenideElement getNewPlusSignButton(Sections section, SelenideElement differentRoot){
        log.info("searching for a new plus sign button for section {}", section);

        return differentRoot.$$("div").filter(attribute("class", section.getSectionElementName())).first().$("button");
    }

    public enum Sections {
        PATH ("section path-section panel-group", "Add a path"),
        DATA_TYPES ("section definition-section panel-group", "Add a data type"),
        RESPONSE ("section responses-section panel-group", "Add a response"),
        CONTACT ("section contact-section panel-group", "Add contact info"),
        LICENSE ("section license-section panel-group", "Set license"),
        TAG ("section tags-section panel-group", "Add a tag"),
        SCHEME ("section security-section panel-group", "Add a security scheme"),
        PROPERTIES ("section security-requirements-section panel-group", "Add a property");

        private String sectionElementName;
        private String a;

        Sections(String sectionElementName, String a) {
            this.sectionElementName = sectionElementName;
            this.a = a;
        }

        public String getSectionElementName() {
            return this.sectionElementName;
        }

        public String getA(){
            return this.a;
        }
    }
}
