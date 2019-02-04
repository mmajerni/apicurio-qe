package apicurito.tests.steps;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import apicurito.tests.utils.slenide.PathUtils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.assertj.core.api.SoftAssertions;
import org.junit.Rule;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;

public class VerificationSteps {

    @Rule
    private SoftAssertions collector = new SoftAssertions();

    @Then("^check all for errors$")
    public void checkAllForErrors() throws Throwable {
        collector.assertAll();
    }

    /**********************************************************************
     *********************** MAIN PAGE verification steps *****************
     **********************************************************************/

    @And("^check that API name is \"([^\"]*)\"$")
    public void checkThatAPINameIs(String expectedName)  {
        String apiName = CommonUtils.getAppRoot().$("title-bar").$("h1").getText();
        collector.assertThat(apiName).as("Checking API name:").isEqualTo(expectedName);
    }

    @And("^check that API version is \"([^\"]*)\"$")
    public void checkThatAPIVersionIs(String expectedVersion) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String version = MainPageUtils.getMainPageRoot().$("info-section").$$("span").filter(attribute("title", "Click to edit.")).first().getText();
        collector.assertThat(version).as("Checking API version:").isEqualTo(expectedVersion);
    }

    @And("^check that API description is \"([^\"]*)\"$")
    public void checkThatAPIDescriptionIs(String expectedDescription) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String description = MainPageUtils.getMainPageRoot().$("info-section").$$("div").filter(attribute("class", "section-field description")).first().getText();
        collector.assertThat(description).as("Checking API descritpion:").isEqualTo(expectedDescription);

    }

    @And("^check that API consume \"([^\"]*)\"$")
    public void checkThatAPIConsume(String expectedConsume){
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String consume = MainPageUtils.getMainPageRoot().$$("div").filter(attribute("class", "section-field consumes")).first().getText();
        collector.assertThat(consume).as("Checking API consume:").isEqualTo(expectedConsume);
    }

    @And("^check that API produce \"([^\"]*)\"$")
    public void checkThatAPIProduce(String expectedProduce){
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        String produce = MainPageUtils.getMainPageRoot().$$("div").filter(attribute("class", "section-field produces")).first().getText();
        collector.assertThat(produce).as("Checking API consume:").isEqualTo(expectedProduce);
    }

    @And("^check that API contact info is$")
    public void checkThatAPIContactInfoIs(DataTable table) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        for (List<String> dataRow : table.raw()) {
            String name = MainPageUtils.getMainPageRoot().$("contact-section").$$("div").filter(attribute("class","section-field name" )).first().getText();
            String email = MainPageUtils.getMainPageRoot().$("contact-section").$$("div").filter(attribute("class","section-field email" )).first().getText();
            String url = MainPageUtils.getMainPageRoot().$("contact-section").$$("div").filter(attribute("class","section-field url" )).first().getText();

            collector.assertThat(name).as("Checking API contatct name:").isEqualTo(dataRow.get(0));
            collector.assertThat(email).as("Checking API contatct email:").isEqualTo(dataRow.get(1));
            collector.assertThat(url).as("Checking API contatct url:").isEqualTo(dataRow.get(2));
        }
    }

    @And("^check that API license is \"([^\"]*)\"$")
    public void checkThatAPILicenseIs(String expectedLincense) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        ElementsCollection licenses = MainPageUtils.getMainPageRoot().$("#license-section-body").$$("button").filter(text("Change License"));
        if (licenses.size() == 1){
            String license = MainPageUtils.getMainPageRoot().$("#license-section-body").$("h2").getText();
            collector.assertThat(license).as("Checking API linces:").isEqualTo(expectedLincense);
        }else{
            collector.fail("License is not set!");
        }
    }

    @And("^check that API have tag \"([^\"]*)\" with description \"([^\"]*)\"$")
    public void checkThatAPIHaveTagWithDescription(String expectedTag, String expectedDescription) {
        CommonUtils.getAppRoot().$("title-bar").$("h1").click();
        ElementsCollection tagRows = MainPageUtils.getMainPageRoot().$("#tags-section-body").$$("tag-row");

        if (tagRows.size() > 0 ) {
            ElementsCollection tag = MainPageUtils.getMainPageRoot().$("#tags-section-body").$$("div").filter(attribute("class", "name")).filter((text(expectedTag)));

            if (tag.size() == 0) {
                collector.fail("Tag %s is not created!", expectedTag);
            } else {
                String desc = tag.first().parent().$$("div").filter(attribute("class", "description")).first().getText();
                collector.assertThat(desc).as("Checking tag description:").isEqualTo(expectedDescription);
            }
        }else{
            collector.fail("Tags are not created!");
        }
    }

    @And("^check that path \"([^\"]*)\" is created$")
    public void checkThatPathIsCreated(String expectedPathName) {
        expectedPathName = expectedPathName.replace("/", "");

        ElementsCollection selectedApi = MainPageUtils.getMainPageRoot().$$("a").filter(attribute("data-target")).first().$$("span").filter(attribute("class", "item-counter"));

        if (selectedApi.size() == 1) {
            ElementsCollection paths = MainPageUtils.getMainPageRoot().$("#path-section-body").findAll(By.xpath("//div[contains(@class, 'api-path ' )]"));//TODO xpath
            List<String> list = new ArrayList<>();
            for (SelenideElement path : paths){
                list.add(path.getText());
            }
            collector.assertThat(list.contains(expectedPathName)).as("Checking if path is created:").isTrue();
        }else{
            collector.fail("Paths are not created!");
        }
    }

    @And("^check that data type \"([^\"]*)\" is created$")
    public void checkThatDataTypeIsCreated(String datatype) {
        ElementsCollection types = MainPageUtils.getMainPageRoot().$("#definition-section-body").findAll(By.xpath("//div[contains(@class, 'api-definition ' )]")).filter(exactText(datatype));//TODO xpath
        collector.assertThat(types.size()).as("Data type %s is not created!", datatype).isEqualTo(1);
    }

    /************************************************************
     ************** PATH PAGE verifications steps ****************
     ************************************************************/

    @And("^check that operation \"([^\"]*)\" is created for path \"([^\"]*)\"$")
    public void checkThatOperationIsCreatedForPath(String operation, String path) {
        SelenideElement pathElement = MainPageUtils.getPathWithName(path);      //TODO private method to check that path is created
        if(pathElement == null){
            collector.assertThat(pathElement).as("Operation %s is not created because path %s is not found.", operation, path).isNotNull();
            return;
        }else{
            pathElement.click();
        }
        ElementsCollection operations = PathUtils.getPathPageRoot().$$("div").filter(attribute("class","api-operation")).filter(text(operation));
        collector.assertThat(operations.size()).as("Operation %s is not created", operation).isEqualTo(1);
    }

    @And("^check that path parameter \"([^\"]*)\" is created for path \"([^\"]*)\"$")
    public void checkThatPathParameterIsCreatedForPath(String parameter, String path) {
        SelenideElement pathElement = MainPageUtils.getPathWithName(path);
        if(pathElement == null){
            collector.assertThat(pathElement).as("Parameter %s is not created because path %s is not found.", parameter, path).isNotNull();
            return;
        }else{
            pathElement.click();
        }

        SelenideElement parameterElement = PathUtils.getPathPageRoot().$("#path-parameters-section-body").$$("path-param-row").filter(matchText(parameter)).first();
        collector.assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not created", parameter).doesNotContain("missing");
    }

    @And("^check that path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = PathUtils.getPathPageRoot().$("#path-parameters-section-body").$$("path-param-row")        //TODO selected description will not work
                .filter(matchText(parameter)).first().$$("div").filter(attribute("class", "description")).first();

        collector.assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }

    @And("^check that path parameter \"([^\"]*)\" has type \"([^\"]*)\" formatted as \"([^\"]*)\"$")
    public void checkThatPathParameterHasTypeFormattedAs(String parameter, String type, String as) {
        SelenideElement summaryElement = PathUtils.getPathPageRoot().$("#path-parameters-section-body").$$("path-param-row")        //TODO selected summary will not work
                .filter(matchText(parameter)).first().$$("div").filter(attribute("class", "summary")).first();

        collector.assertThat(summaryElement.getText()).as("Parameter has not type %s formatted as %s", type, as).isEqualTo(type.toLowerCase() + " as " + as.toLowerCase());
                                                                                                                                        //TODO need support for array
                                                                                                                                        //TODO e.g. int32 will not work (different strings in summary)
                                                                                                                                        //TODO if both type and as are the same ->fail
    }

    /**************************************************************
     ************* OPERATION PAGE verification steps **************
     **************************************************************/

    @And("^check that operation summary is \"([^\"]*)\"$")
    public void checkThatOperationSummaryIs(String expectedSummary) {
        String summary = OperationUtils.getOperationRoot().$$("div").filter(attribute("class", "section-field summary")).first().getText();
        collector.assertThat(summary).as("Checking operation summary:").isEqualTo(expectedSummary);
    }

    @And("^check that operation ID is \"([^\"]*)\"$")
    public void checkThatOperationIDIs(String expectedID) {
        String id = OperationUtils.getOperationRoot().$$("div").filter(attribute("class", "section-field operationId")).first().getText();
        collector.assertThat(id).as("Checking operation ID:").isEqualTo(expectedID);
    }

    @And("^check that operation description is \"([^\"]*)\"$")
    public void checkThatOperationDescriptionIs(String expectedDescription) {
        String description = OperationUtils.getOperationRoot().$$("div").filter(attribute("class", "section-field description")).first().getText();
        collector.assertThat(description).as("Checking operation description:").isEqualTo(expectedDescription);
    }

    /**
     * Tags must be in same order
     * @param expectedTags separate tags with ","
     *
     */
    @And("^check that operation tags are \"([^\"]*)\"$")
    public void checkThatOperationTagsAre(String expectedTags){
        String[] list = expectedTags.split(",");
        ElementsCollection ec = OperationUtils.getOperationRoot().$$("span").filter(attribute("class", "label label-default"));
        for ( int i = 0; i < ec.size(); ++i ){
            collector.assertThat(ec.get(i).getText()).as("Checking %d. tag:",i+1).isEqualTo(list[i]);
        }
    }

    @And("^check that exist response (\\d+) with description \"([^\"]*)\" and type \"([^\"]*)\" as \"([^\"]*)\"$")
    public void checkThatExistResponseWithDescriptionAndTypeAs(Integer expectedResponse, String expectedDescription, String expectedType, String expectedAs) {
        ElementsCollection rows = OperationUtils.getOperationRoot().$$("response-row").filter(text(expectedResponse.toString()));
        if (rows.size() == 1 ){
            String description =  rows.first().find(By.xpath("div/div/div[contains(@class, 'description')]")).getText();
            collector.assertThat(description).as("Checking description:").isEqualTo(expectedDescription);

            String types = rows.first().find(By.xpath("div/div/div[contains(@class, 'summary' )]")).getText();
            collector.assertThat(types).as("Checking types:").isEqualTo(expectedType.toLowerCase() + " as " + expectedAs.toLowerCase());
                                                                                                            //TODO need support for array
                                                                                                            //TODO e.g. int32 will not work (different strings in summary)
                                                                                                            //TODO if both type and as are the same ->fail
        }else{
            collector.fail("Response %s is not created!",expectedResponse);
        }
    }

    @And("^check that path parameter \"([^\"]*)\" is overridden$")
    public void checkThatPathParameterIsOverridden(String parameter) {
        SelenideElement parameterElement = OperationUtils.getOperationRoot().$("#path-parameters-section-body").$$("path-param-row").filter(matchText(parameter)).first();
        collector.assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not overridden", parameter).doesNotContain("overridable");
    }

    @And("^check that overridden path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatOverriddenPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = OperationUtils.getOperationRoot().$("#path-parameters-section-body").$$("path-param-row")        //TODO selected description will not work
                .filter(matchText(parameter)).first().$$("div").filter(attribute("class", "description")).first();

        collector.assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }
}


