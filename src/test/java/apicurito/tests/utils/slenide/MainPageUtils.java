package apicurito.tests.utils.slenide;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainPageUtils {

    public static SelenideElement INFO_SECTION = getMainPageRoot().$("info-section");
    public static SelenideElement CONTACT_SECTION = getMainPageRoot().$("contact-section");
    public static SelenideElement LICENSE_SECTION = getMainPageRoot().$("license-section");
    public static SelenideElement TAGS_SECTION = getMainPageRoot().$("tags-section");
    public static SelenideElement SECURITY_SECTION = getMainPageRoot().$("security-schemes-section");
    public static SelenideElement REQUIREMENTS_SECTION = getMainPageRoot().$("security-requirements-section");

    public static SelenideElement getMainPageRoot() {
        return $("editor").shouldBe(visible);
    }

    public static void setLicense(String license) {
        log.info("Setting license to {}", license);

        SelenideElement parentElement = LICENSE_SECTION.$$("a").filter(text(license)).first().parent().parent();
        parentElement.$$("button").filter(text("Use This License")).first().click();
    }

    public static void addTag(String tag, String description) {
        log.info("Adding tag {} with description {}", tag, description);

        TAGS_SECTION.$("#tag").setValue(tag);
        TAGS_SECTION.$("#description").setValue(description);
        CommonUtils.getButtonWithText("Add", TAGS_SECTION).click();
    }

    public static SelenideElement getPathWithName(String pathName) {
        log.info("Getting path with name {}", pathName);

        ElementsCollection allPaths = CommonUtils.getAppRoot().$$("div")
                .filter(attribute("class", "section-body")).first()
                .findAll(By.xpath("//div[contains(@class, 'api-path ' )]")); //TODO xpath
        for (SelenideElement path : allPaths) {
            String actualPathName = path.$$("div").filter(attribute("path-item")).first().getText();
            if (actualPathName.equals(pathName)) {
                return path.$("div");
            }
        }
        //path is not found
        return null;
    }

    public static SelenideElement getDataTypeWithName(String datatypeName) {
        log.info("Getting data type with name {}", datatypeName);

        return CommonUtils.getAppRoot().$$("section").filter(attribute("label", "Data Types")).first()
                .$$(By.className("api-definition")).filter(text(datatypeName)).first();
    }

    public static void createDataType(String name, String description, Boolean isRest) {
        log.info("Creating data type with name {} and description {} and rest resources are {}", name, description, isRest);

        CommonUtils.getLabelWithName("name", CommonUtils.getAppRoot()).setValue(name);
        //TODO CommonUtils.getAppRoot().$("textarea").setValue(description);
        if (isRest) {
            CommonUtils.getAppRoot().$$("div").filter(attribute("class", "create-option")).filter(text("REST Resource")).first().click();
        }
        CommonUtils.getButtonWithText("Save", CommonUtils.getAppRoot().$("#server-entity-form")).click();
    }

    public static void putSearchSubstring(String substring) {
        log.info("Searching for {}", substring);
        CommonUtils.getAppRoot().$("#masterSearch").setValue(substring);
    }

    public static void cancelSearching() {
        log.info("Canceling searching");

        CommonUtils.getAppRoot().$("search").$$("button").filter(attribute("class", "clear")).first().click();
    }
}
