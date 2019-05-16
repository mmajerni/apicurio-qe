package apicurito.tests.steps.verification;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import apicurito.tests.steps.DataTypeSteps;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.DataTypeUtils;
import cucumber.api.java.en.Then;

public class DataTypeVerifications {

    @Then("^check that data type description is \"([^\"]*)\"$")
    public void checkThatDataTypeDescriptionIs(String expectedDescription) {
        String description = DataTypeUtils.getDataTypesRoot().$(DataTypeSteps.INFO_SECTION).$(By.className("description")).getText();
        CollectorHelper.getCollector().assertThat(description).as("Description should be %s but is %s", expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that data type property \"([^\"]*)\" is created$")
    public void checkThatDataTypePropertyIsCreated(String expectedProperty) {
        ElementsCollection properties = DataTypeSteps.PROPERTIES_SECTION.$$("property-row");
        for (SelenideElement property : properties) {
            String name = property.$(By.className("name")).getText();
            if (name.equals(expectedProperty)) {
                return;
            }
        }
        CollectorHelper.getCollector().fail("Property with name %s is not created", expectedProperty);
    }

    @Then("^check that description is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatDescriptionIsForProperty(String expectedDescription, String property) {
        DataTypeUtils.openPropertyDescription(property);
        String description = DataTypeSteps.PROPERTIES_SECTION.$$("property-row").filter(Condition.text(property)).first().$(By.className("description")).getText();
        CollectorHelper.getCollector().assertThat(description).as("Description of property %s should be %s but is %s", property, expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that type is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeIsForProperty(String expectedType, String property) {
        DataTypeUtils.openPropertyTypes(property);
        String type = DataTypeSteps.PROPERTIES_SECTION.$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(type).as("Type is %s but should be %s for data type property %s", type, expectedType, property).isEqualTo(expectedType);
    }

    @Then("^check that type of is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeOfIsForProperty(String expectedOf, String property) {
        DataTypeUtils.openPropertyTypes(property);
        String of = DataTypeSteps.PROPERTIES_SECTION.$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(of).as("Type is %s but should be %s for data type property %s", of, expectedOf, property).isEqualTo(expectedOf);
    }

    @Then("^check that type as is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeAsIsForProperty(String expectedAs, String property) {
        DataTypeUtils.openPropertyTypes(property);
        String as = DataTypeSteps.PROPERTIES_SECTION.$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(as).as("Type is %s but should be %s for data type property %s", as, expectedAs, property).isEqualTo(expectedAs);
    }

    @Then("^check that property \"([^\"]*)\" is \"([^\"]*)\"$")
    public void checkThatPropertyIsRequired(String property, String expectedIsRequired) {
        DataTypeUtils.openPropertyTypes(property);
        String isRequired = DataTypeSteps.PROPERTIES_SECTION.$(CommonUtils.DropdownButtons.PROPERTY_REQUIRED.getButtonId()).getText();
        CollectorHelper.getCollector().assertThat(isRequired).as("Property %s should be %s but is %s", property, expectedIsRequired, isRequired)
                .isEqualTo(expectedIsRequired);
    }

    @Then("^check that example is \"([^\"]*)\"$")
    public void checkThatExampleIs(String expectedExample) {
        String example = DataTypeUtils.getDataTypesRoot().$(DataTypeSteps.EXAMPLE_SECTION).$(By.className("example")).getText();
        CollectorHelper.getCollector().assertThat(example).as("Example should be %s but is %s", expectedExample, example).isEqualTo(expectedExample);
    }
}
