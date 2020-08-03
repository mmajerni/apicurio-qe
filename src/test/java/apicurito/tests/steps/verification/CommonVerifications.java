package apicurito.tests.steps.verification;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.MainPageUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import apicurito.tests.utils.slenide.PathUtils;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;

import javax.xml.crypto.Data;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.visible;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CommonVerifications {

    private static class Elements {
        private static By DESCRIPTION = By.className("description");
        private static By PARAMETERS_TYPE = By.className("param-type");

        private static By SERVER_ROW = By.cssSelector("server-row");
    }

    /*
        Check that exist parameters or RDFs (Request data forms) on specified page
        param could be header|query|RFD
        page could be path|operations
     */
    @Then("check that exist {string} on {string} page")
    public void checkThatExistOnPage(String param, String page, DataTable table) {
        SelenideElement pageElement = page.equals("operations") ? OperationUtils.getOperationRoot() : PathUtils.getPathPageRoot();
        String rowType = null;
        By section = CommonUtils.getSectionBy(param);

        switch (param) {
            case "query":
                rowType = "query-param-row";
                break;
            case "header":
                rowType = "header-param-row";
                break;
            case "RDF":
                rowType = "formdata-param-row";
                break;
            case "cookie":
                rowType = "cookie-param-row";
                break;
        }

        CommonUtils.openCollapsedSection(pageElement, section);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, pageElement, section, rowType, param + " parameter");
        }
    }

    @Then("check parameters types")
    public void checkParametersTypes(DataTable table) {
        for (List<String> dataRow : table.cells()) {
            String buttonId = CommonUtils.getButtonId(dataRow.get(0));
            SelenideElement page = CommonUtils.getPageElement(dataRow.get(2));
            By section = CommonUtils.getSectionBy(dataRow.get(3));

            if (Boolean.valueOf(dataRow.get(4))) {
                OperationUtils.selectResponse(dataRow.get(5));
            } else {
                CommonUtils.openCollapsedSection(page, section);      //TODO open the right row
            }

            String dropDownValue = page.$(section).$(buttonId).getText();
            assertThat(dropDownValue).as("%s is %s but should be %s", dataRow.get(0), dropDownValue, dataRow.get(1)).isEqualTo(dataRow.get(1));
        }
    }

    @Then("^check that API \"([^\"]*)\" values \"([^\"]*)\" on page \"([^\"]*)\"$")
    public void checkThatConsumesProducesHasValuesOnPage(String consumesProduces, String values, String page) {
        SelenideElement subsection = CommonUtils.getPageElement(page).$(By.className(consumesProduces));
        assertThat(subsection.getText()).as("%s should be %s but is %s", consumesProduces, values, subsection.getText()).isEqualTo(values);
    }

    @Then("check that {string} created {string} on {string} page with name {string}")
    public void checkThatCreatedOnPageWithName(String isCreated, String param, String page, String elementName) {
        By sectionBy = CommonUtils.getSectionBy(param);
        SelenideElement elementRow = CommonUtils.getElementRow(CommonUtils.getPageElement(page).$(sectionBy), param, elementName);
        if ("is".equals(isCreated)) {
            assertThat(elementRow).as("Object %s with name %s on %s page is not created and should be", param, elementName, page).isNotNull();
        } else {
            assertThat(elementRow).as("Object %s with name %s on %s page is created and should not be", param, elementName, page).isNull();
        }
    }

    @Then("check that {string} overridden {string} on {string} page with name {string}")
    public void checkThatOverrideOnPageWithName(String isOverridden, String param, String page, String elementName) {
        By sectionBy = CommonUtils.getSectionBy(param);
        SelenideElement elementRow = CommonUtils.getElementRow(CommonUtils.getPageElement(page).$(sectionBy), param, elementName);
        if ("is".equals(isOverridden)) {
            assertThat(elementRow.$$("button").filter(text("Override"))).as("Object %s with name %s on %s page is not created and should be", param, elementName, page).isNull();
        } else {
            assertThat(elementRow.$$("button").filter(text("Override"))).as("Object %s with name %s on %s page is created and should not be", param, elementName, page).isNotNull();
        }
    }

    private void checkRow(List<String> dataRow, SelenideElement pageElement, By section, String rowType, String message) {
        ElementsCollection queryRows = pageElement.$(section).$$(rowType);

        if (queryRows.size() > 0) {
            ElementsCollection names = pageElement.$(section).$$(By.className("name")).filter((text(dataRow.get(0))));

            if (names.size() == 0) {
                fail("%s with name %s is not created!", message, dataRow.get(0));
            } else {
                SelenideElement row = names.first().parent().parent();

                if (!dataRow.get(1).isEmpty()) {
                    String description = row.$(Elements.DESCRIPTION).getText();
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
                    String type = row.$(Elements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
                    assertThat(type).as("%s type is %s but should be %s", message, type, dataRow.get(3)).isEqualTo(dataRow.get(3));
                }

                if (!dataRow.get(4).isEmpty()) {
                    String of = row.$(Elements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
                    assertThat(of).as("%s type of is %s but should be %s", message, of, dataRow.get(4)).isEqualTo(dataRow.get(4));
                }

                if (!dataRow.get(5).isEmpty()) {
                    String as = row.$(Elements.PARAMETERS_TYPE).$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
                    assertThat(as).as("%s type as is %s but should be %s", message, as, dataRow.get(5)).isEqualTo(dataRow.get(5));
                }
            }
        } else {
            fail("There is no %s!", message);
        }
    }

    @Then("check that server was created on {string} page")
    public void checkThatServerWasCreatedOnPage(String page, DataTable table) {
        By section = CommonUtils.getSectionBy("server");
        SelenideElement pageElement = CommonUtils.getPageElement(page);
        CommonUtils.openCollapsedSection(pageElement, section);

        for (List<String> dataRow : table.cells()) {
            ElementsCollection servers = pageElement.$(section).$$(Elements.SERVER_ROW);
            assertThat(servers.size()).as("No servers were found!").isGreaterThan(0);

            boolean found = false;
            for (SelenideElement serverRow : servers) {
                if (serverRow.$(By.className("url")).getText().equals(dataRow.get(0))) {
                    if (!dataRow.get(1).isEmpty()) {
                        String description = serverRow.$(By.className("description")).getText();
                        assertThat(description).as("Server description is %s but it should be %s", description, dataRow.get(1)).isEqualTo(dataRow.get(1));
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("Server with url %s was not found", dataRow.get(0));
            }
        }
    }
}
