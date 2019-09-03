package apicurito.tests.steps;

import org.openqa.selenium.By;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.DataTypeUtils;
import cucumber.api.java.en.When;

public class DataTypeSteps {

    public static SelenideElement PROPERTIES_SECTION = DataTypeUtils.getDataTypesRoot().$$("section")
            .filter(Condition.attribute("label", "PROPERTIES")).first();

    private static class DataTypesElements {
        private static By INFO_SECTION = By.cssSelector("definition-info-section");
        private static By EXAMPLE_SECTION = By.cssSelector("definition-example-section");
    }

    @When("^create data type property with name \"([^\"]*)\"$")
    public void createDataTypeProperty(String property) {
        CommonUtils.getClickableLink(CommonUtils.Sections.PROPERTIES, PROPERTIES_SECTION).click();
        CommonUtils.getLabelWithName("name", DataTypeUtils.getDataTypesRoot()).sendKeys(property);
        CommonUtils.getButtonWithText("Save", CommonUtils.getAppRoot().$("property-editor")).click();
    }

    @When("^set description \"([^\"]*)\" for data type property \"([^\"]*)\"$")
    public void setDescriptionForProperty(String description, String property) {
        DataTypeUtils.openPropertyDescription(property);
        CommonUtils.setValueInTextArea(description, PROPERTIES_SECTION);
    }

    @When("^set data type description \"([^\"]*)\"$")
    public void setDataTypeDescription(String description) {
        CommonUtils.setValueInTextArea(description, DataTypeUtils.getDataTypesRoot().$(DataTypesElements.INFO_SECTION));
    }

    @When("^set data type example \"([^\"]*)\"$")
    public void setDataTypeExample(String example) {
        CommonUtils.setValueInTextArea(example, DataTypeUtils.getDataTypesRoot().$(DataTypesElements.EXAMPLE_SECTION));
    }

    @When("^set property \"([^\"]*)\" as \"([^\"]*)\"$")
    public void setPropertyAsRequired(String property, String isRequired) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_REQUIRED.getButtonId(), isRequired, PROPERTIES_SECTION);
    }

    @When("^set property type \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeForProperty(String type, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE.getButtonId(), type, PROPERTIES_SECTION);
    }

    @When("^set property type of \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeOfForProperty(String of, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_OF.getButtonId(), of, PROPERTIES_SECTION);
    }

    @When("^set property type as \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeAsForProperty(String as, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.PROPERTY_TYPE_AS.getButtonId(), as, PROPERTIES_SECTION);
    }
}
