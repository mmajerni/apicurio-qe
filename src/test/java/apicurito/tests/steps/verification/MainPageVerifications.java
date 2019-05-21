package apicurito.tests.steps.verification;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Then("^check that API name is \"([^\"]*)\"$")
    public void checkThatAPINameIs(String expectedName) {       //NEW
        String apiName = CommonUtils.getAppRoot().$("title-bar").$("h1").getText();
        assertThat(apiName).as("Checking API name:").isEqualTo(expectedName);
    }

    @Then("^check that API version is \"([^\"]*)\"$")
    public void checkThatAPIVersionIs(String expectedVersion) {     //NEW
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String version = MainPageUtils.getMainPageRoot().$("info-section").$(By.className("version")).getText();
        assertThat(version).as("Checking API version:").isEqualTo(expectedVersion);
    }

    @Then("^check that API description is \"([^\"]*)\"$")
    public void checkThatAPIDescriptionIs(String expectedDescription) {     //TODO
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String description = MainPageUtils.getMainPageRoot().$("info-section").$(By.className("description")).getText();
        CollectorHelper.getCollector().assertThat(description).as("Checking API descritpion:").isEqualTo(expectedDescription);
    }

    @Then("^check that API consume \"([^\"]*)\"$")
    public void checkThatAPIConsume(String expectedConsume) {               //TODO      //NEW
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String consume = MainPageUtils.getMainPageRoot().$(By.className("consumes")).getText();
        assertThat(consume).as("Checking API consumes:").isEqualTo(expectedConsume);
    }

    @Then("^check that API produce \"([^\"]*)\"$")
    public void checkThatAPIProduce(String expectedProduce) {       //NEW
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String produce = MainPageUtils.getMainPageRoot().$(By.className("produces")).getText();
        assertThat(produce).as("Checking API produces:").isEqualTo(expectedProduce);
    }

    @Then("^check that API contact info is$")
    public void checkThatAPIContactInfoIs(DataTable table) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        for (List<String> dataRow : table.cells()) {
            String name = MainPageUtils.getMainPageRoot().$("contact-section").$(By.className("name")).getText();
            String email = MainPageUtils.getMainPageRoot().$("contact-section").$(By.className("email")).getText();
            String url = MainPageUtils.getMainPageRoot().$("contact-section").$(By.className("url")).getText();

            CollectorHelper.getCollector().assertThat(name).as("Checking API contatct name:").isEqualTo(dataRow.get(0));
            CollectorHelper.getCollector().assertThat(email).as("Checking API contatct email:").isEqualTo(dataRow.get(1));
            CollectorHelper.getCollector().assertThat(url).as("Checking API contatct url:").isEqualTo(dataRow.get(2));
        }
    }

    @Then("^check that API license is \"([^\"]*)\"$")
    public void checkThatAPILicenseIs(String expectedLincense) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        ElementsCollection licenses = MainPageUtils.getMainPageRoot().$("license-section").$$("button").filter(text("Change License"));
        if (licenses.size() == 1) {
            String license = MainPageUtils.getMainPageRoot().$("license-section").$("h2").getText();
            CollectorHelper.getCollector().assertThat(license).as("Checking API linces:").isEqualTo(expectedLincense);
        } else {
            CollectorHelper.getCollector().fail("License is not set!");
        }
    }

    @Then("^check that API have tag \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void checkThatAPIHaveTagWithDescription(String expectedTag, String expectedDescription) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        ElementsCollection tagRows = MainPageUtils.getMainPageRoot().$("tags-section").$$("tag-row");

        if (tagRows.size() > 0) {
            ElementsCollection tag = MainPageUtils.getMainPageRoot().$("tags-section").$$(By.className("name")).filter((text(expectedTag)));

            if (tag.size() == 0) {
                CollectorHelper.getCollector().fail("Tag %s is not created!", expectedTag);
            } else {
                String desc = tag.first().parent().$(By.className("description")).getText();
                CollectorHelper.getCollector().assertThat(desc).as("Checking tag description:").isEqualTo(expectedDescription);
            }
        } else {
            CollectorHelper.getCollector().fail("Tags are not created!");
        }
    }

    @Then("^check that path \"([^\"]*)\" is created$")
    public void checkThatPathIsCreated(String expectedPathName) {       //NEW
        try {
            Thread.sleep(1000L);        //need to wait at least for a second because of Stale Element Reference Exception
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ElementsCollection paths = MainPageUtils.getMainPageRoot().$$("section").filter(attribute("label", "Paths")).first()
                .$$(By.className("api-path")).filter(exactText(expectedPathName));

        assertThat(paths.size()).as("Path %s is not created:", expectedPathName).isEqualTo(1);
    }

    @Then("^check that data type \"([^\"]*)\" is created$")
    public void checkThatDataTypeIsCreated(String datatype) {       //NEW
        ElementsCollection types = MainPageUtils.getMainPageRoot().$$("section").filter(attribute("label", "Data Types")).first()
                .$$(By.className("api-definition")).filter(exactText(datatype));
        assertThat(types.size()).as("Data type %s is not created!", datatype).isEqualTo(1);
    }

    /**************************************************************
     ********** SECURITY SUBSECTION verification steps ************
     **************************************************************/
    @Then("^check security scheme$")
    public void checkSecurityScheme(DataTable table) {
        for (List<String> dataRow : table.cells()) {
            ElementsCollection schemeElements = MainPageUtils.getMainPageRoot().$(MainPageUtils.SECURITY_SECTION).$$("security-scheme-row").filter(text(dataRow.get(0))).filter(text(dataRow.get(1)));
            CollectorHelper.getCollector().assertThat(schemeElements.size()).as("Scheme with name %s do not exist", dataRow.get(0)).isEqualTo(1);
            if (!dataRow.get(2).isEmpty()) {
                String description = schemeElements.first().$(By.className("description")).getText();
                CollectorHelper.getCollector().assertThat(description).as("Description should be %s but is %s", dataRow.get(2), description).isEqualTo(dataRow.get(2));
            }
        }
    }

    @Then("^check that API security requirement \"([^\"]*)\" exists$")
    public void checkThatAPISecurityRequirementExists(String requirement) {
        ElementsCollection requirementList = MainPageUtils.getMainPageRoot().$(MainPageUtils.REQUIREMENTS_SECTION).$$(By.className("security-requirement")).filter(text(requirement));
        CollectorHelper.getCollector().assertThat(requirementList.size()).as("Requirement %s do not exist", requirement).isEqualTo(1);
    }
}
