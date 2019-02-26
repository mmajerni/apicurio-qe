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
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainPageSteps {
    /**
     * Second column: if true then create with link else create with plus sign.
     */
    @And("^create a new path with link$")
    public void createNewPathWithLink(DataTable table) {
        for (List<String> dataRow : table.raw()) {
            if (Boolean.valueOf(dataRow.get(1))) {
                CommonUtils.getClickableLink(CommonUtils.Sections.PATH, CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                        .click();
            } else {
                CommonUtils.getNewPlusSignButton(CommonUtils.Sections.PATH, CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                        .click();
            }

            CommonUtils.getLabelWithName("path", CommonUtils.getAppRoot().shouldBe(visible, enabled).shouldNotHave(attribute("disabled")))
                    .setValue("/" + dataRow.get(0));
            CommonUtils.getButtonWithText("Add", CommonUtils.getAppRoot()).shouldBe(visible, enabled).shouldNotHave(attribute("disabled"))
                    .click();
        }
    }

    @And("^change API name to \"([^\"]*)\"$")
    public void changeAPINameTo(String newName) {
        SelenideElement se = CommonUtils.getAppRoot().$(By.className("editor-title-bar-edit"));
        se.hover();
        se.click();

        CommonUtils.getLabelWithName("title-input", CommonUtils.getAppRoot())
                .setValue(newName);
        CommonUtils.getButtonWithTitle("Save changes.", CommonUtils.getAppRoot().$("title-bar"))
                .click();
    }

    @And("^set API version to \"([^\"]*)\"$")
    public void setAPIVersionTo(String version) {
        CommonUtils.setValueInLabel(version, CommonUtils.getAppRoot().$("info-section").$(By.className("version")), false);
    }

    @And("^change description to \"([^\"]*)\"$")
    public void changeDescriptionTo(String description) {
        CommonUtils.setValueInTextArea(description, CommonUtils.getAppRoot().$("info-section").$(By.className("description")));
    }

    @And("^add contact info$")
    public void addContactInfo(DataTable table) {
        CommonUtils.getClickableLink(CommonUtils.Sections.CONTACT, MainPageUtils.CONTACT_SECTION).click();
        for (List<String> dataRow : table.raw()) {
            CommonUtils.setValueInLabel(dataRow.get(0), CommonUtils.getAppRoot().$("contact-section").$(By.className("name")), false);
            CommonUtils.setValueInLabel(dataRow.get(1), CommonUtils.getAppRoot().$("contact-section").$(By.className("email")), false);
            CommonUtils.setValueInLabel(dataRow.get(2), CommonUtils.getAppRoot().$("contact-section").$(By.className("url")), false);
        }
    }

    @And("^add license \"([^\"]*)\"$")
    public void addLicense(String license) {
        CommonUtils.getClickableLink(CommonUtils.Sections.LICENSE, MainPageUtils.LICENSE_SECTION).click();
        MainPageUtils.setLicense(license);
    }

    @And("^add tag \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void addTagWithDescription(String tag, String description) {
        CommonUtils.getClickableLink(CommonUtils.Sections.TAG, MainPageUtils.TAGS_SECTION).click();
        MainPageUtils.addTag(tag, description);
    }

    @And("^select path \"([^\"]*)\"$")
    public void selectPath(String path) {
        MainPageUtils.getPathWithName(path).click();
    }

    @And("^set consumes to \"([^\"]*)\"$")      //TODO this is add consume but application/json is still there --> delete it
    public void setConsumesTo(String consumes) {
        log.info("Setting consumes to {}", consumes);
        CommonUtils.setValueInLabel(consumes, MainPageUtils.getMainPageRoot().$(By.className("consumes")), true);
    }

    @And("^set produces to \"([^\"]*)\"$")      //TODO this is add consume but application/json is still there --> delete it
    public void setProducesTo(String produces) {
        log.info("Setting produces to {}", produces);
        CommonUtils.setValueInLabel(produces, MainPageUtils.getMainPageRoot().$(By.className("produces")), true);
    }

    /**
     * @param table parameters: Name | Description | boolean if should be created with REST resources | boolean if should be created with Link |
     */
    @And("^create a new data type with link$")
    public void createANewDataType(DataTable table) {
        for (List<String> dataRow : table.raw()) {

            if (Boolean.valueOf(dataRow.get(3))) {
                CommonUtils.getClickableLink(CommonUtils.Sections.DATA_TYPES, CommonUtils.getAppRoot()).click();
            } else {
                CommonUtils.getNewPlusSignButton(CommonUtils.Sections.DATA_TYPES, CommonUtils.getAppRoot()).click();
            }

            MainPageUtils.createDataType(dataRow.get(0), dataRow.get(1), Boolean.valueOf(dataRow.get(2)));
        }
    }

    @And("^search path or data type with substring \"([^\"]*)\"$")
    public void searchPathOrDataTypeWithSubstring(String substring) {
        MainPageUtils.putSearchSubstring(substring);
    }

    @And("^cancel searching$")
    public void cancelSearching() {
        MainPageUtils.cancelSearching();
    }

    @And("^select data type \"([^\"]*)\"$")
    public void selectDataType(String dataTypeName) {
        MainPageUtils.getDataTypeWithName(dataTypeName).click();
    }

    @And("^create basic security scheme with values$")
    public void createBasicSecuritySchemeWithValues(DataTable table) {
        for (List<String> dataRow : table.raw()) {
            CommonUtils.getNewPlusSignButton(CommonUtils.Sections.SCHEME, MainPageUtils.SECURITY_SECTION)
                    .click();
            SelenideElement schemeEditor = MainPageUtils.getMainPageRoot().$("security-scheme-editor");

            CommonUtils.getLabelWithName("schemeName", schemeEditor)
                    .setValue(dataRow.get(0));

            if (!dataRow.get(1).isEmpty()) {
                schemeEditor.$("#description").setValue(dataRow.get(1));
            }

            CommonUtils.setDropDownValue("button", "BASIC", schemeEditor.$(By.className("dropdown")));

            CommonUtils.getButtonWithText("Save", schemeEditor)
                    .click();
        }
    }

    @And("^create API Key security scheme with values$")
    public void createAPIKeySecuritySchemeWithValues(DataTable table) {
        for (List<String> dataRow : table.raw()) {
            CommonUtils.getNewPlusSignButton(CommonUtils.Sections.SCHEME, MainPageUtils.SECURITY_SECTION)
                    .click();
            SelenideElement schemeEditor = MainPageUtils.getMainPageRoot().$("security-scheme-editor");

            CommonUtils.getLabelWithName("schemeName", schemeEditor)
                    .setValue(dataRow.get(0));

            if (!dataRow.get(1).isEmpty()) {
                schemeEditor.$("#description").setValue(dataRow.get(1));
            }
            CommonUtils.setDropDownValue("button", "API Key", schemeEditor.$(By.className("dropdown")));

            if (!dataRow.get(2).isEmpty()) {
                CommonUtils.setDropDownValue("#in20", dataRow.get(2), schemeEditor.$(By.className("apiKey-auth")));
            }

            if (!dataRow.get(3).isEmpty()) {
                CommonUtils.getLabelWithType("text", schemeEditor.$(By.className("apiKey-auth")))
                        .setValue(dataRow.get(3));
            }
            CommonUtils.getButtonWithText("Save", schemeEditor)
                    .click();
        }
    }

    @And("^create OAuth security scheme with values$")
    public void createOAuthSecuritySchemeWithValues(DataTable table) {
        for (List<String> dataRow : table.raw()) {
            CommonUtils.getNewPlusSignButton(CommonUtils.Sections.SCHEME, MainPageUtils.SECURITY_SECTION)
                    .click();
            SelenideElement schemeEditor = MainPageUtils.getMainPageRoot().$("security-scheme-editor");

            CommonUtils.getLabelWithName("schemeName", schemeEditor)
                    .setValue(dataRow.get(0));

            if (!dataRow.get(1).isEmpty()) {
                schemeEditor.$("#description").setValue(dataRow.get(1));
            }
            CommonUtils.setDropDownValue("button", "OAuth 2", schemeEditor.$(By.className("dropdown")));

            if (!dataRow.get(2).isEmpty()) {
                CommonUtils.setDropDownValue("#flow", dataRow.get(2), schemeEditor.$(By.className("oauth2-auth")));
            }

            if (!dataRow.get(3).isEmpty()) {
                CommonUtils.getLabelWithName("authorizationUrl", schemeEditor.$(By.className("oauth2-auth")))
                        .setValue(dataRow.get(3));
            }

            if (!dataRow.get(3).isEmpty()) {
                CommonUtils.getLabelWithName("tokenUrl", schemeEditor.$(By.className("oauth2-auth")))
                        .setValue(dataRow.get(4));
            }
            CommonUtils.getButtonWithText("Save", schemeEditor)
                    .click();
        }
    }

    @And("^create security requirement with schemes$")
    public void createSecurityRequirementWithSchemes(DataTable table) {
        CommonUtils.getNewPlusSignButton(CommonUtils.Sections.REQUIREMENT, MainPageUtils.REQUIREMENTS_SECTION)
                .click();
        SelenideElement requirementEditor = MainPageUtils.getMainPageRoot().$("security-requirement-editor");

        for (List<String> dataRow : table.raw()) {
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

    @And("^add scopes to security requirement \"([^\"]*)\" and OAuth scheme \"([^\"]*)\"$")
    public void addScopesToSecurityRequirementAndOAuthScheme(String requirementName, String schemeName, DataTable table) {
        SelenideElement requirement = MainPageUtils.REQUIREMENTS_SECTION.$$(By.className("security-requirement")).filter(text(requirementName)).first();
        requirement.$("button").click();
        requirement.$("a").shouldHave(text("Edit")).click();

        SelenideElement requirementEditor = MainPageUtils.getMainPageRoot().$("security-requirement-editor");
        SelenideElement scheme = requirementEditor.$$(By.className("list-group-item")).filter(text(schemeName)).first();
        scheme.$(By.className("list-view-pf-expand")).$("span").click();
        for (List<String> dataRow : table.raw()) {
            scheme.$$(By.className("scope")).filter(text(dataRow.get(0))).first().$("input").click();
        }
        CommonUtils.getButtonWithText("Save", requirementEditor)
                .click();
    }

    @And("^add scopes to scheme \"([^\"]*)\"$")
    public void addScopesToScheme(String scheme, DataTable table) {
        SelenideElement schemeElement = MainPageUtils.SECURITY_SECTION.$$("security-scheme-row").filter(text(scheme)).first();
        schemeElement.$("button").click();
        schemeElement.$("a").shouldHave(text("Edit")).click();

        SelenideElement schemeEditor = MainPageUtils.getMainPageRoot().$("security-scheme-editor");
        for (List<String> dataRow : table.raw()) {
            CommonUtils.getButtonWithText("Add Scope", schemeEditor).click();
            ElementsCollection scopeElements = schemeEditor.$$(By.className("scope"));

            CommonUtils.setValueInLabel(dataRow.get(0), scopeElements.get(scopeElements.size() - 1).$(By.className("scope-name")), false);
            CommonUtils.setValueInLabel(dataRow.get(1), scopeElements.get(scopeElements.size() - 1).$(By.className("scope-description")), false);
        }
        CommonUtils.getButtonWithText("Save", schemeEditor)
                .click();
    }
}
