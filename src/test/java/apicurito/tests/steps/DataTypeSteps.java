package apicurito.tests.steps;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.DataTypeUtils;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.And;

public class DataTypeSteps {

    private static SelenideElement PROPERTIES_SECTION = DataTypeUtils.getDataTypesRoot().$$("section")
            .filter(Condition.attribute("label", "PROPERTIES")).first();
    private static SelenideElement INFO_SECTION = DataTypeUtils.getDataTypesRoot().$("definition-info-section");
    private static SelenideElement EXAMPLE_SECTION = DataTypeUtils.getDataTypesRoot().$("definition-example-section");

    @And("^create data type property with name \"([^\"]*)\"$")
    public void createDataTypeProperty(String property){
        CommonUtils.getClickableLink(CommonUtils.Sections.PROPERTIES, PROPERTIES_SECTION).click();
        CommonUtils.getLabelWithName("name", DataTypeUtils.getDataTypesRoot()).setValue(property);
        CommonUtils.getButtonWithText("Save", CommonUtils.getAppRoot().$("property-editor")).click();
    }

    @And("^set description \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setDescriptionForProperty(String description, String property) {
        DataTypeUtils.openPropertyDescription(property);
        CommonUtils.setValueInTextArea(description, PROPERTIES_SECTION);
    }

    @And("^set data type description \"([^\"]*)\"$")
    public void setDataTypeDescription(String description) {
        CommonUtils.setValueInTextArea(description, INFO_SECTION);
    }

    @And("^set data type example \"([^\"]*)\"$")
    public void setDataTypeExample(String example) {
        CommonUtils.setValueInTextArea(example, EXAMPLE_SECTION);
    }

    @And("^set property \"([^\"]*)\" as required \"([^\"]*)\"$")
    public void setPropertyAsRequired(String property, boolean isRequired) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.REQUIRED.getButtonId(), isRequired ? "Required" : "Not Required", PROPERTIES_SECTION);

    }

    @And("^set property type \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeForProperty(String type, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE.getButtonId(), type, PROPERTIES_SECTION);
    }

    @And("^set property type of \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeOfForProperty(String of, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE_OF.getButtonId(), of, PROPERTIES_SECTION);
    }

    @And("^set property type as \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setPropertyTypeAsForProperty(String as, String property) {
        DataTypeUtils.openPropertyTypes(property);
        CommonUtils.setDropDownValue(CommonUtils.DropdownButtons.TYPE_AS.getButtonId(), as, PROPERTIES_SECTION);
    }
}
