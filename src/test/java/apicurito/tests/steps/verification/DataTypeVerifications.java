package apicurito.tests.steps.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static com.codeborne.selenide.Condition.text;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import apicurito.tests.steps.DataTypeSteps;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.DataTypeUtils;
import cucumber.api.java.en.Then;

public class DataTypeVerifications {

    private static class DataTypesElements {
        private static By INFO_SECTION = By.cssSelector("definition-info-section");
        private static By EXAMPLE_SECTION = By.cssSelector("definition-example-section");

        private static By PROPERTY_ROW = By.cssSelector("property-row");
        private static By DESCRIPTION = By.className("description");
    }

    @Then("^check that data type description is \"([^\"]*)\"$")
    public void checkThatDataTypeDescriptionIs(String expectedDescription) {        //NEW
        String description = DataTypeUtils.getDataTypesRoot().$(DataTypesElements.INFO_SECTION).$(DataTypesElements.DESCRIPTION).getText();
        assertThat(description).as("Description should be %s but is %s", expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that data type property \"([^\"]*)\" is created$")
    public void checkThatDataTypePropertyIsCreated(String expectedProperty) {       //NEW
        ElementsCollection properties = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW);
        for (SelenideElement property : properties) {
            String name = property.$(By.className("name")).getText();
            if (name.equals(expectedProperty)) {
                return;
            }
        }
        fail("Property with name %s is not created", expectedProperty);
    }

    @Then("^check that description is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatDescriptionIsForProperty(String expectedDescription, String property) {        //NEW
        DataTypeUtils.openPropertyDescription(property);
        String description = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW).filter(Condition.text(property)).first().$(DataTypesElements.DESCRIPTION).getText();
        assertThat(description).as("Description of property %s should be %s but is %s", property, expectedDescription, description).isEqualTo(expectedDescription);
    }

    @Then("^check that type is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeIsForProperty(String expectedType, String property) {      //NEW
        DataTypeUtils.openPropertyTypes(property);
        String type = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW)
                .filter(text(property)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId()).getText();
        assertThat(type).as("Type is %s but should be %s for data type property %s", type, expectedType, property).isEqualTo(expectedType);
    }

    @Then("^check that type of is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeOfIsForProperty(String expectedOf, String property) {      //NEW
        DataTypeUtils.openPropertyTypes(property);
        String of = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW)
                .filter(text(property)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId()).getText();
        assertThat(of).as("Type is %s but should be %s for data type property %s", of, expectedOf, property).isEqualTo(expectedOf);
    }

    @Then("^check that type as is \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void checkThatTypeAsIsForProperty(String expectedAs, String property) {      //NEW
        DataTypeUtils.openPropertyTypes(property);
        String as = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW)
                .filter(text(property)).first().$(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId()).getText();
        assertThat(as).as("Type is %s but should be %s for data type property %s", as, expectedAs, property).isEqualTo(expectedAs);
    }

    @Then("^check that property \"([^\"]*)\" is \"([^\"]*)\"$")
    public void checkThatPropertyIsRequired(String property, String expectedIsRequired) {       //NEW
        DataTypeUtils.openPropertyTypes(property);
        String isRequired = DataTypeSteps.PROPERTIES_SECTION.$$(DataTypesElements.PROPERTY_ROW)
                .filter(text(property)).first().$(CommonUtils.DropdownButtons.PROPERTY_REQUIRED.getButtonId()).getText();
        assertThat(isRequired).as("Property %s should be %s but is %s", property, expectedIsRequired, isRequired)
                .isEqualTo(expectedIsRequired);
    }

    @Then("^check that example is (.*)$")
    public void checkThatExampleIsRegEx(String expectedExample) {        //NEW
        String example = DataTypeUtils.getDataTypesRoot().$(DataTypesElements.EXAMPLE_SECTION).$(By.className("example")).getText();
        assertThat(example).as("Example should be %s but is %s", expectedExample, example).isEqualTo(expectedExample);
    }
}
