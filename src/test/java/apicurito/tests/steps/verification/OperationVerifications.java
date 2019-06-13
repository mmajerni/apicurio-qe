package apicurito.tests.steps.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;

public class OperationVerifications {

    private static class OperationElements {
        private static By DESCRIPTION = By.className("description");

        private static By PATH_PARAMETERS_SECTION = By.cssSelector("path-params-section");
        private static By RESPONSES_SECTION = By.cssSelector("responses-section");
        private static By REQUEST_BODY_SECTION = By.cssSelector("requestbody-section");
        private static By QUERY_PARAM_SECTION = By.cssSelector("query-params-section");
        private static By HEADER_PARAM_SECTION = By.cssSelector("header-params-section");
        private static By REQUIREMENTS_SECTION = By.cssSelector("security-requirements-section");

        private static By PATH_PARAMETERS_ROW = By.cssSelector("path-param-row");
        private static By PARAMETERS_TYPE = By.className("param-type");
    }

    @Then("^check that operation summary is \"([^\"]*)\"$")
    public void checkThatOperationSummaryIs(String expectedSummary) {       //NEW
        String summary = OperationUtils.getOperationRoot().$(By.className("summary")).getText();
        assertThat(summary).as("Checking operation summary:").isEqualTo(expectedSummary);
    }

    @Then("^check that operation ID is \"([^\"]*)\"$")
    public void checkThatOperationIDIs(String expectedID) {     //NEW
        String id = OperationUtils.getOperationRoot().$(By.className("operationId")).getText();
        assertThat(id).as("Checking operation ID:").isEqualTo(expectedID);
    }

    @Then("^check that operation description is \"([^\"]*)\"$")
    public void checkThatOperationDescriptionIs(String expectedDescription) {       //NEW
        String description = OperationUtils.getOperationRoot().$(OperationElements.DESCRIPTION).getText();
        assertThat(description).as("Checking operation description:").isEqualTo(expectedDescription);
    }

    /**
     * Tags must be in same order
     *
     * @param expectedTags separate tags with ","
     */
    @Then("^check that operation tags are \"([^\"]*)\"$")
    public void checkThatOperationTagsAre(String expectedTags) {
        String[] list = expectedTags.split(",");
        ElementsCollection ec = OperationUtils.getOperationRoot().$(By.className("tags")).$$(By.className("label-default"));
        for (int i = 0; i < ec.size(); ++i) {
            CollectorHelper.getCollector().assertThat(ec.get(i).getText()).as("Checking %d. tag:", i + 1).isEqualTo(list[i]);
        }
    }

    @Then("^check that path parameter \"([^\"]*)\" is overridden$")
    public void checkThatPathParameterIsOverridden(String parameter) {
        SelenideElement parameterElement = OperationUtils.getOperationRoot().$(OperationElements.PATH_PARAMETERS_SECTION).$$(OperationElements.PATH_PARAMETERS_ROW).filter(matchText(parameter)).first();
        CollectorHelper.getCollector().assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not overridden", parameter).doesNotContain("overridable");
    }

    @Then("^check that overridden path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatOverriddenPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = OperationUtils.getOperationRoot().$(OperationElements.PATH_PARAMETERS_SECTION).$$(OperationElements.PATH_PARAMETERS_ROW)
                .filter(matchText(parameter)).first().$(OperationElements.DESCRIPTION);

        CollectorHelper.getCollector().assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }

    @Then("^check that exist response (\\d+)$")
    public void checkThatExistResponse(Integer response) {      //NEW
        ElementsCollection rows = OperationUtils.getOperationRoot().$$(By.className("statusCode")).filter(text(response.toString()));
        assertThat(rows.size()).as("Response %d is not exist", response).isEqualTo(1);
    }

    @Then("^check that description is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatDescriptionIsForResponse(String expectedDescription, Integer response) {       //NEW
        OperationUtils.selectResponse(response);
        String description = OperationUtils.getOperationRoot().$(OperationElements.RESPONSES_SECTION).$(By.className("response-description")).$(By.className("grow")).getText();
        assertThat(description).as("Checking description:").isEqualTo(expectedDescription);
    }

    @Then("^check that type is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeIsForResponse(String expectedType, Integer response) {     //NEW
        OperationUtils.selectResponse(response);
        String type = OperationUtils.getOperationRoot().$(OperationElements.RESPONSES_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        assertThat(type).as("Type is %s but should be %s", type, expectedType).isEqualTo(expectedType);
    }

    @Then("^check that type of is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeOfIsForResponse(String expectedOf, Integer response) {     //NEW
        OperationUtils.selectResponse(response);
        String of = OperationUtils.getOperationRoot().$(OperationElements.RESPONSES_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        assertThat(of).as("Type of is %s but should be %s", of, expectedOf).isEqualTo(expectedOf);
    }

    @Then("^check that type as is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeAsIsForResponse(String expectedAs, Integer response) {     //NEW
        OperationUtils.selectResponse(response);
        String as = OperationUtils.getOperationRoot().$(OperationElements.RESPONSES_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        assertThat(as).as("Type as is %s but should be %s", as, expectedAs).isEqualTo(expectedAs);
    }

    @Then("^check that operation consumes \"([^\"]*)\"$")
    public void checkThatOperationConsumes(String consumes) {       //NEW
        SelenideElement consumesSubsection = OperationUtils.getOperationRoot().$(By.className("consumes")); //TODO in OperationStep is the same thing find better solution
        assertThat(consumesSubsection.getText()).as("Overriden consumes should be %s but is %s", consumes, consumesSubsection.getText()).isEqualTo(consumes);
    }

    @Then("^check that operation produces \"([^\"]*)\"$")
    public void checkThatOperationProduces(String produces) {       //NEW
        SelenideElement producesSubsection = OperationUtils.getOperationRoot().$(By.className("produces")); //TODO in OperationStep is the same thing find better solution
        assertThat(producesSubsection.getText()).as("Overriden consumes should be %s but is %s", produces, producesSubsection.getText()).isEqualTo(produces);
    }

    @Then("^check that exist request body$")
    public void checkThatExistRequestBody() {       //NEW
        ElementsCollection collapsedSection = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$$("a").filter(attribute("class", "collapsed"));
        if (collapsedSection.size() > 0) {
            collapsedSection.first().click();
        }

        ElementsCollection rb = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$$(By.className("request-body-type"));
        assertThat(rb.size()).as("Request body is not created!").isEqualTo(1);
    }

    @Then("^check that request body description is \"([^\"]*)\"$")
    public void checkThatRequestBodyDescriptionIs(String expectedDescription) {     //NEW
        String description = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$("inline-markdown-editor").getText();
        assertThat(description).as("Request body description should be %s but is %s", expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that request body type is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeIs(String expectedType) {       //NEW
        String type = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        assertThat(type).as("Type is %s but should be %s", type, expectedType).isEqualTo(expectedType);
    }

    @Then("^check that request body type of is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeOfIs(String expectedOf) {       //NEW
        String of = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        assertThat(of).as("Type of is %s but should be %s", of, expectedOf).isEqualTo(expectedOf);
    }

    @Then("^check that request body type as is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeAsIs(String expectedAs) {       //NEW
        String as = OperationUtils.getOperationRoot().$(OperationElements.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        assertThat(as).as("Type as is %s but should be %s", as, expectedAs).isEqualTo(expectedAs);
    }

    @Then("^check that exist request form data$")
    public void checkThatExistRequestFormData(DataTable table) {        //NEW
        CommonUtils.openCollapsedSection(OperationElements.REQUEST_BODY_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationElements.REQUEST_BODY_SECTION, "formdata-param-row", "Form data parameter");
        }
    }

    @Then("^check that exist query parameters$")
    public void checkThatExistQueryParameter(DataTable table) {
        CommonUtils.openCollapsedSection(OperationElements.QUERY_PARAM_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationElements.QUERY_PARAM_SECTION, "query-param-row", "Query parameter");
        }
    }

    @Then("^check that exist header parameters$")
    public void checkThatExistHeaderParameters(DataTable table) {
        CommonUtils.openCollapsedSection(OperationElements.HEADER_PARAM_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationElements.HEADER_PARAM_SECTION, "header-param-row", "Header parameter");
        }
    }

    private void checkRow(List<String> dataRow, By section, String rowType, String message) {       //NEW
        ElementsCollection queryRows = OperationUtils.getOperationRoot().$(section).$$(rowType);

        if (queryRows.size() > 0) {
            ElementsCollection names = OperationUtils.getOperationRoot().$(section).$$(By.className("name")).filter((text(dataRow.get(0))));

            if (names.size() == 0) {
                fail("%s with name %s is not created!", message, dataRow.get(0));
            } else {
                SelenideElement row = names.first().parent().parent();

                if (!dataRow.get(1).isEmpty()) {
                    String description = row.$(OperationElements.DESCRIPTION).getText();
                    assertThat(description).as("%s description should be %s but is %s", message, dataRow.get(1), description).isEqualTo(dataRow.get(1));
                }

                row.$(By.className("summary")).click();

                if (!dataRow.get(2).isEmpty()) {
                    String isRequired = row.$(By.className("param-required")).$("drop-down").getText();
                    assertThat(isRequired)
                            .as("%s should be %s but is %s", message, dataRow.get(2), isRequired)
                            .isEqualTo(dataRow.get(2));
                }

                if (!dataRow.get(3).isEmpty()) {
                    String type = row.$(OperationElements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
                    assertThat(type).as("%s type is %s but should be %s", message, type, dataRow.get(3)).isEqualTo(dataRow.get(3));
                }

                if (!dataRow.get(4).isEmpty()) {
                    String of = row.$(OperationElements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
                    CollectorHelper.getCollector().assertThat(of).as("%s type of is %s but should be %s", message, of, dataRow.get(4)).isEqualTo(dataRow.get(4));
                }

                if (!dataRow.get(5).isEmpty()) {
                    String as = row.$(OperationElements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
                    assertThat(as).as("%s type as is %s but should be %s", message, as, dataRow.get(5)).isEqualTo(dataRow.get(5));
                }
            }
        } else {
            fail("There is no %s!", message);
        }
    }

    @Then("^check that operation security requirement \"([^\"]*)\" exist$")
    public void checkThatOperationSecurityRequirementExist(String requirement) {        //TODO same method as in MainPageVerifications --> join         //NEW
        ElementsCollection requirementList = OperationUtils.getOperationRoot().$(OperationElements.REQUIREMENTS_SECTION).$$(By.className("security-requirement")).filter(text(requirement));
        assertThat(requirementList.size()).as("Operation requirement %s do not exist", requirement).isEqualTo(1);
    }
}
