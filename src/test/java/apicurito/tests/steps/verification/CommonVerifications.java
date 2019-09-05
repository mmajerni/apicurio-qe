package apicurito.tests.steps.verification;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import apicurito.tests.utils.slenide.PathUtils;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CommonVerifications {

    private static class Elements {
        private static By QUERY_PARAM_SECTION = By.cssSelector("query-params-section");
        private static By HEADER_PARAM_SECTION = By.cssSelector("header-params-section");
        private static By REQUEST_BODY_SECTION = By.cssSelector("requestbody-section");
        private static By DESCRIPTION = By.className("description");
        private static By PARAMETERS_TYPE = By.className("param-type");
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
        By section = null;

        switch (param) {
            case "query":
                rowType = "query-param-row";
                section = Elements.QUERY_PARAM_SECTION;
                break;
            case "header":
                rowType = "header-param-row";
                section = Elements.HEADER_PARAM_SECTION;
                break;
            case "RFD":
                rowType = "formdata-param-row";
                section = Elements.REQUEST_BODY_SECTION;
                break;
        }

        CommonUtils.openCollapsedSection(pageElement, section);

        for (List<String> dataRow : table.cells()) {
            checkRow(dataRow, pageElement, section, rowType, param + " parameter");
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
}
