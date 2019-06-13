package apicurito.tests.steps.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;

import java.util.List;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;

public class MainPageVerifications {

    private static class MainPageElements {
        private static By TITLE_BAR = By.cssSelector("title-bar");
        private static By H1 = By.cssSelector("h1");

        private static By INFO_SECTION = By.cssSelector("info-section");
        private static By CONTACT_SECTION = By.cssSelector("contact-section");
        private static By LICENSE_SECTION = By.cssSelector("license-section");
        private static By TAGS_SECTION = By.cssSelector("tags-section");
        private static By SECURITY_SECTION = By.cssSelector("security-schemes-section");
        private static By REQUIREMENTS_SECTION = By.cssSelector("security-requirements-section");

        private static By DESCRIPTION = By.className("description");
        private static By SECTION = By.cssSelector("section");
    }

    @Then("^check that API name is \"([^\"]*)\"$")
    public void checkThatAPINameIs(String expectedName) {
        String apiName = CommonUtils.getAppRoot().$(MainPageElements.TITLE_BAR).$(MainPageElements.H1).getText();
        assertThat(apiName).as("Checking API name:").isEqualTo(expectedName);
    }

    @Then("^check that API version is \"([^\"]*)\"$")
    public void checkThatAPIVersionIs(String expectedVersion) {
        openMainPage();
        String version = MainPageUtils.getMainPageRoot().$(MainPageElements.INFO_SECTION).$(By.className("version")).getText();
        assertThat(version).as("Checking API version:").isEqualTo(expectedVersion);
    }

    @Then("^check that API description is \"([^\"]*)\"$")
    public void checkThatAPIDescriptionIs(String expectedDescription) {
        openMainPage();
        String description = MainPageUtils.getMainPageRoot().$(MainPageElements.INFO_SECTION).$(MainPageElements.DESCRIPTION).getText();
        assertThat(description).as("Checking API descritpion:").isEqualTo(expectedDescription);
    }

    @Then("^check that API consume \"([^\"]*)\"$")
    public void checkThatAPIConsume(String expectedConsume) {               //TODO      //NEW
        openMainPage();
        String consume = MainPageUtils.getMainPageRoot().$(By.className("consumes")).getText();
        assertThat(consume).as("Checking API consumes:").isEqualTo(expectedConsume);
    }

    @Then("^check that API produce \"([^\"]*)\"$")
    public void checkThatAPIProduce(String expectedProduce) {       //NEW
        openMainPage();
        String produce = MainPageUtils.getMainPageRoot().$(By.className("produces")).getText();
        assertThat(produce).as("Checking API produces:").isEqualTo(expectedProduce);
    }

    @Then("^check that API contact info is$")
    public void checkThatAPIContactInfoIs(DataTable table) {
        openMainPage();
        for (List<String> dataRow : table.cells()) {
            String name = MainPageUtils.getMainPageRoot().$(MainPageElements.CONTACT_SECTION).$(By.className("name")).getText();
            assertThat(name).as("Checking API contact name:").isEqualTo(dataRow.get(0));

            String email = MainPageUtils.getMainPageRoot().$(MainPageElements.CONTACT_SECTION).$(By.className("email")).getText();
            assertThat(email).as("Checking API contact email:").isEqualTo(dataRow.get(1));

            String url = MainPageUtils.getMainPageRoot().$(MainPageElements.CONTACT_SECTION).$(By.className("url")).getText();
            assertThat(url).as("Checking API contact url:").isEqualTo(dataRow.get(2));
        }
    }

    @Then("^check that API license is \"([^\"]*)\"$")
    public void checkThatAPILicenseIs(String expectedLincense) {
        openMainPage();
        ElementsCollection licenses = MainPageUtils.getMainPageRoot().$(MainPageElements.LICENSE_SECTION).$$("button").filter(text("Change License"));
        if (licenses.size() == 1) {
            String license = MainPageUtils.getMainPageRoot().$(MainPageElements.LICENSE_SECTION).$("h2").getText();
            assertThat(license).as("Checking API linces:").isEqualTo(expectedLincense);
        } else {
            fail("License is not set!");
        }
    }

    @Then("^check that API have tag \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void checkThatAPIHaveTagWithDescription(String expectedTag, String expectedDescription) {
        openMainPage();
        ElementsCollection tagRows = MainPageUtils.getMainPageRoot().$(MainPageElements.TAGS_SECTION).$$("tag-row");

        if (tagRows.size() > 0) {
            ElementsCollection tag = MainPageUtils.getMainPageRoot().$(MainPageElements.TAGS_SECTION).$$(By.className("name")).filter((text(expectedTag)));

            if (tag.size() == 0) {
                fail("Tag %s is not created!", expectedTag);
            } else {
                String desc = tag.first().parent().$(MainPageElements.DESCRIPTION).getText();
                assertThat(desc).as("Checking tag description:").isEqualTo(expectedDescription);
            }
        } else {
            fail("Tags are not created!");
        }
    }

    @Then("^check that path \"([^\"]*)\" is created$")
    public void checkThatPathIsCreated(String expectedPathName) {
        try {
            Thread.sleep(1000L);        //need to wait at least for a second because of Stale Element Reference Exception
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ElementsCollection paths = MainPageUtils.getMainPageRoot().$$(MainPageElements.SECTION).filter(attribute("label", "Paths")).first()
                .$$(By.className("api-path")).filter(exactText(expectedPathName));

        assertThat(paths.size()).as("Path %s is not created:", expectedPathName).isEqualTo(1);
    }

    @Then("^check that data type \"([^\"]*)\" is created$")
    public void checkThatDataTypeIsCreated(String datatype) {
        ElementsCollection types = MainPageUtils.getMainPageRoot().$$(MainPageElements.SECTION).filter(attribute("label", "Data Types")).first()
                .$$(By.className("api-definition")).filter(exactText(datatype));
        assertThat(types.size()).as("Data type %s is not created!", datatype).isEqualTo(1);
    }

    /**************************************************************
     ********** SECURITY SUBSECTION verification steps ************
     **************************************************************/

    /**
     * Currently is supported just name and description without editing scheme.
     * #TODO after closing: https://github.com/Apicurio/apicurio-studio/issues/656
     */
    @Then("^check security scheme$")
    public void checkSecurityScheme(DataTable table) {
        openMainPage();
        for (List<String> dataRow : table.cells()) {
            ElementsCollection schemeElements = MainPageUtils.getMainPageRoot().$(MainPageElements.SECURITY_SECTION).$$("security-scheme-row").filter(text(dataRow.get(0))).filter(text(dataRow.get(1)));
            assertThat(schemeElements.size()).as("Scheme with name %s do not exist", dataRow.get(0)).isEqualTo(1);
            if (!dataRow.get(2).isEmpty()) {
                String description = schemeElements.first().$(MainPageElements.DESCRIPTION).getText();
                assertThat(description).as("Description should be %s but is %s", dataRow.get(2), description).isEqualTo(dataRow.get(2));
            }
        }
    }

    @Then("^check that API security requirement \"([^\"]*)\" exists$")
    public void checkThatAPISecurityRequirementExists(String requirement) {
        openMainPage();
        ElementsCollection requirementList = MainPageUtils.getMainPageRoot().$(MainPageElements.REQUIREMENTS_SECTION).$$(By.className("security-requirement")).filter(text(requirement));
        assertThat(requirementList.size()).as("Requirement %s do not exist", requirement).isEqualTo(1);
    }

    private void openMainPage() {
        CommonUtils.getAppRoot().$(MainPageElements.TITLE_BAR).$(MainPageElements.H1).click();
    }
}
