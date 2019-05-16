package apicurito.tests.steps.verification;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;

import org.openqa.selenium.By;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import apicurito.tests.steps.OperationSteps;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;

public class OperationVerifications {

    @Then("^check that operation summary is \"([^\"]*)\"$")
    public void checkThatOperationSummaryIs(String expectedSummary) {
        String summary = OperationUtils.getOperationRoot().$(By.className("summary")).getText();
        CollectorHelper.getCollector().assertThat(summary).as("Checking operation summary:").isEqualTo(expectedSummary);
    }

    @Then("^check that operation ID is \"([^\"]*)\"$")
    public void checkThatOperationIDIs(String expectedID) {
        String id = OperationUtils.getOperationRoot().$(By.className("operationId")).getText();
        CollectorHelper.getCollector().assertThat(id).as("Checking operation ID:").isEqualTo(expectedID);
    }

    @Then("^check that operation description is \"([^\"]*)\"$")
    public void checkThatOperationDescriptionIs(String expectedDescription) {
        String description = OperationUtils.getOperationRoot().$(By.className("description")).getText();
        CollectorHelper.getCollector().assertThat(description).as("Checking operation description:").isEqualTo(expectedDescription);
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
        SelenideElement parameterElement = OperationUtils.getOperationRoot().$("path-params-section").$$("path-param-row").filter(matchText(parameter)).first();
        CollectorHelper.getCollector().assertThat(parameterElement.$("div").getAttribute("class")).as("Path parameter %s is not overridden", parameter).doesNotContain("overridable");
    }

    @Then("^check that overridden path parameter \"([^\"]*)\" has description \"([^\"]*)\"$")
    public void checkThatOverriddenPathParameterHasDescription(String parameter, String description) {
        SelenideElement descriptionElement = OperationUtils.getOperationRoot().$("path-params-section").$$("path-param-row")
                .filter(matchText(parameter)).first().$(By.className("description"));

        CollectorHelper.getCollector().assertThat(descriptionElement.getText()).as("Description for path parameter %s is different", parameter).isEqualTo(description);
    }

    @Then("^check that exist response (\\d+)$")
    public void checkThatExistResponse(Integer response) {
        ElementsCollection rows = OperationUtils.getOperationRoot().$$(By.className("statusCode")).filter(text(response.toString()));
        CollectorHelper.getCollector().assertThat(rows.size()).as("Response %d is not exist", response).isEqualTo(1);
    }

    @Then("^check that description is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatDescriptionIsForResponse(String expectedDescription, Integer response) {
        OperationUtils.selectResponse(response);
        String description = OperationUtils.getOperationRoot().$("responses-section").$(By.className("response-description")).$(By.className("grow")).getText();
        CollectorHelper.getCollector().assertThat(description).as("Checking description:").isEqualTo(expectedDescription);
    }

    @Then("^check that type is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeIsForResponse(String expectedType, Integer response) {
        OperationUtils.selectResponse(response);
        String type = OperationUtils.getOperationRoot().$("responses-section").$("#api-property-type").getText();
        CollectorHelper.getCollector().assertThat(type).as("Type is %s but should be %s", type, expectedType).isEqualTo(expectedType);
    }

    @Then("^check that type of is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeOfIsForResponse(String expectedOf, Integer response) {
        OperationUtils.selectResponse(response);
        String of = OperationUtils.getOperationRoot().$("responses-section").$("#api-property-type-of").getText();
        CollectorHelper.getCollector().assertThat(of).as("Type of is %s but should be %s", of, expectedOf).isEqualTo(expectedOf);
    }

    @Then("^check that type as is \"([^\"]*)\" for response (\\d+)$")
    public void checkThatTypeAsIsForResponse(String expectedAs, Integer response) {
        OperationUtils.selectResponse(response);
        String as = OperationUtils.getOperationRoot().$("responses-section").$("#api-property-type-as").getText();
        CollectorHelper.getCollector().assertThat(as).as("Type as is %s but should be %s", as, expectedAs).isEqualTo(expectedAs);
    }

    @Then("^check that operation consumes \"([^\"]*)\"$")
    public void checkThatOperationConsumes(String consumes) {
        SelenideElement CONSUMES_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("consumes")); //TODO in OperationStep is the same thing find better solution
        CollectorHelper.getCollector().assertThat(CONSUMES_SUBSECTION.getText()).as("Overriden consumes should be %s but is %s", consumes, CONSUMES_SUBSECTION.getText()).isEqualTo(consumes);
    }

    @Then("^check that operation produces \"([^\"]*)\"$")
    public void checkThatOperationProduces(String produces) {
        SelenideElement PRODUCES_SUBSECTION = OperationUtils.getOperationRoot().$(By.className("produces")); //TODO in OperationStep is the same thing find better solution
        CollectorHelper.getCollector().assertThat(PRODUCES_SUBSECTION.getText()).as("Overriden consumes should be %s but is %s", produces, PRODUCES_SUBSECTION.getText()).isEqualTo(produces);
    }

    @Then("^check that exist request body$")
    public void checkThatExistRequestBody() {
        ElementsCollection collapsedSection = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$$("a").filter(attribute("class", "collapsed"));
        if (collapsedSection.size() > 0) {
            collapsedSection.first().click();
        }

        ElementsCollection rb = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$$(By.className("request-body-type"));
        CollectorHelper.getCollector().assertThat(rb.size()).as("Request body is not created!").isEqualTo(1);
    }

    @Then("^check that request body description is \"([^\"]*)\"$")
    public void checkThatRequestBodyDescriptionIs(String expectedDescription) {
        String description = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$("inline-markdown-editor").getText();
        CollectorHelper.getCollector().assertThat(description).as("Request body description should be %s but is %s", expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that request body type is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeIs(String expectedType) {
        String type = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(type).as("Type is %s but should be %s", type, expectedType).isEqualTo(expectedType);
    }

    @Then("^check that request body type of is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeOfIs(String expectedOf) {
        String of = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(of).as("Type of is %s but should be %s", of, expectedOf).isEqualTo(expectedOf);
    }

    @Then("^check that request body type as is \"([^\"]*)\"$")
    public void checkThatRequestBodyTypeAsIs(String expectedAs) {
        String as = OperationUtils.getOperationRoot().$(OperationSteps.REQUEST_BODY_SECTION).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(as).as("Type as is %s but should be %s", as, expectedAs).isEqualTo(expectedAs);
    }

    @Then("^check that exist request form data$")
    public void checkThatExistRequestFormData(DataTable table) {
        CommonUtils.openCollapsedSection(OperationSteps.REQUEST_BODY_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationSteps.REQUEST_BODY_SECTION, "formdata-param-row", "Form data parameter");
        }
    }

    @Then("^check that exist query parameters$")
    public void checkThatExistQueryParameter(DataTable table) {
        CommonUtils.openCollapsedSection(OperationSteps.QUERY_PARAM_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationSteps.QUERY_PARAM_SECTION, "query-param-row", "Query parameter");
        }
    }

    @Then("^check that exist header parameters$")
    public void checkThatExistHeaderParameters(DataTable table) {
        CommonUtils.openCollapsedSection(OperationSteps.HEADER_PARAM_SECTION);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, OperationSteps.HEADER_PARAM_SECTION, "header-param-row", "Header parameter");
        }
    }

    private void checkRow(List<String> dataRow, By section, String rowType, String message) {
        ElementsCollection queryRows = OperationUtils.getOperationRoot().$(section).$$(rowType);

        if (queryRows.size() > 0) {
            ElementsCollection names = OperationUtils.getOperationRoot().$(section).$$(By.className("name")).filter((text(dataRow.get(0))));

            if (names.size() == 0) {
                CollectorHelper.getCollector().fail("%s with name %s is not created!", message, dataRow.get(0));
            } else {
                SelenideElement row = names.first().parent().parent();

                if (!dataRow.get(1).isEmpty()) {
                    String description = row.$(By.className("description")).getText();
                    CollectorHelper.getCollector().assertThat(description).as("%s description should be %s but is %s", message, dataRow.get(1), description).isEqualTo(dataRow.get(1));
                }

                row.$(By.className("summary")).click();

                if (!dataRow.get(2).isEmpty()) {
                    String isRequired = row.$(By.className("param-required")).$("drop-down").getText();
                    CollectorHelper.getCollector().assertThat(isRequired)
                            .as("%s should be %s but is %s", message, dataRow.get(2), isRequired)
                            .isEqualTo(dataRow.get(2));
                }

                if (!dataRow.get(3).isEmpty()) {
                    String type = row.$(By.className("param-type")).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
                    CollectorHelper.getCollector().assertThat(type).as("%s type is %s but should be %s", message, type, dataRow.get(3)).isEqualTo(dataRow.get(3));
                }

                if (!dataRow.get(4).isEmpty()) {
                    String of = row.$(By.className("param-type")).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
                    CollectorHelper.getCollector().assertThat(of).as("%s type of is %s but should be %s", message, of, dataRow.get(4)).isEqualTo(dataRow.get(4));
                }

                if (!dataRow.get(5).isEmpty()) {
                    String as = row.$(By.className("param-type")).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
                    CollectorHelper.getCollector().assertThat(as).as("%s type as is %s but should be %s", message, as, dataRow.get(5)).isEqualTo(dataRow.get(5));
                }
            }
        } else {
            CollectorHelper.getCollector().fail("There is no %s!", message);
        }
    }

    @Then("^check that operation security requirement \"([^\"]*)\" exist$")
    public void checkThatOperationSecurityRequirementExist(String requirement) {        //TODO same method as in MainPageVerifications --> join
        ElementsCollection requirementList = OperationUtils.getOperationRoot().$(MainPageUtils.REQUIREMENTS_SECTION).$$(By.className("security-requirement")).filter(text(requirement));
        CollectorHelper.getCollector().assertThat(requirementList.size()).as("Operation requirement %s do not exist", requirement).isEqualTo(1);
    }
}
