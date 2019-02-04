package apicurito.tests.utils.slenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPageUtils {

    public static SelenideElement INFO_SECTION = getMainPageRoot().$("info-section");
    public static SelenideElement CONTACT_SECTION = getMainPageRoot().$("contact-section");
    public static SelenideElement LICENSE_SECTION = getMainPageRoot().$("license-section");
    public static SelenideElement TAGS_SECTION = getMainPageRoot().$("tags-section");
    public static SelenideElement SECURITY_SECTION = getMainPageRoot().$("security-schemes-section");
    public static SelenideElement REQUIREMENTS_SECTION = getMainPageRoot().$("security-requirements-section");

    public static SelenideElement getMainPageRoot() { return $("editor").shouldBe(visible); }

    public static void setVersion(String version){
        INFO_SECTION.$$("span").filter(attribute("title", "Click to edit.")).first().click();
        INFO_SECTION.$("input").setValue(version);
        CommonUtils.getButtonWithTitle("Save changes.",getMainPageRoot()).click();
    }

    public static void changeDescription(String description){
        INFO_SECTION.$$("div").filter(attribute("class", "section-field description")).first()
                .$$("div").filter(attribute("title", "Click to edit.")).first().click();
        //INFO_SECTION.$("ace-editor textarea").clear(); //TODO do not work
        INFO_SECTION.$("ace-editor textarea").sendKeys(Keys.CONTROL + "a");
        INFO_SECTION.$("ace-editor textarea").sendKeys(Keys.DELETE);

        INFO_SECTION.$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", INFO_SECTION).click();
    }

    public static void setContactInformation(String input, String form){
        CommonUtils.getLabelWithName(form, CONTACT_SECTION.$("#setcontact-form")).setValue(input);
    }

    public static void setLicense(String license){
        SelenideElement parentElement = LICENSE_SECTION.$$("a").filter(text(license)).first().parent().parent();
        parentElement.$$("button").filter(text("Use This License")).first().click();
    }

    public static void addTag(String tag, String description){
        TAGS_SECTION.$("#tag").setValue(tag);
        TAGS_SECTION.$("#description").setValue(description);
        CommonUtils.getButtonWithText("Add", TAGS_SECTION).click();
    }

    public static SelenideElement getPathWithName(String pathName){
        pathName = pathName.replace("/", "");

        ElementsCollection allPaths = CommonUtils.getAppRoot().$("#path-section-body").findAll(By.xpath("//div[contains(@class, 'api-path ' )]")); //TODO xpath
        for (SelenideElement path : allPaths){
            String actualPathName = path.$$("div").filter(attribute("path-item")).first().getText();
           if (actualPathName.equals( pathName )){
               return path.$("div");
           }
        }
        //path is not found
        return null;
    }

    public static SelenideElement getDataTypeWithName(String datatypeName){
        return CommonUtils.getAppRoot().$("#definition-section-body").$$("div").filter(text(datatypeName)).first();
    }

    public static void setConsumesProduces(Boolean isConsumes, String value){
        SelenideElement pencil;
        if (isConsumes) {
             pencil = INFO_SECTION.$$("div").filter(attribute("class", "section-field consumes")).first().$("i");
        }else{
             pencil = INFO_SECTION.$$("div").filter(attribute("class", "section-field produces")).first().$("i");
        }

        pencil.click();
        pencil.click();    // double click does not work
        INFO_SECTION.$("input").setValue(value);
        CommonUtils.getButtonWithTitle("Save changes.", INFO_SECTION).click();

    }

    public static void createDataType(String name, String description, Boolean isRest){
        CommonUtils.getLabelWithName("name", CommonUtils.getAppRoot()).setValue(name);
        //TODO CommonUtils.getAppRoot().$("textarea").setValue(description);
        if(isRest) {
            CommonUtils.getAppRoot().$$("div").filter(attribute("class", "create-option")).filter(text("REST Resource")).first().click();
        }
        CommonUtils.getButtonWithText("Save", CommonUtils.getAppRoot().$("#server-entity-form")).click();
    }

    public static void putSearchSubstring(String substring){
        CommonUtils.getAppRoot().$("#masterSearch").setValue(substring);
    }

    public static void cancelSearching(){
        CommonUtils.getAppRoot().$("search").$$("button").filter(attribute("class", "clear")).first().click();
    }

}
