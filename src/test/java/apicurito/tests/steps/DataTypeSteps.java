package apicurito.tests.steps;
import apicurito.tests.utils.slenide.CommonUtils;
import apicurito.tests.utils.slenide.DataTypeUtils;
import apicurito.tests.utils.slenide.OperationUtils;
import com.codeborne.selenide.SelenideElement;
import cucumber.api.java.en.And;

public class DataTypeSteps {

    private static SelenideElement PROPERTIES_SECTION = DataTypeUtils.getDataTypesRoot().$("#properties-section-body");
    private static SelenideElement INFO_SECTION = DataTypeUtils.getDataTypesRoot().$("#info-section-body");
    private static SelenideElement EXAMPLE_SECTION = DataTypeUtils.getDataTypesRoot().$("definition-example-section");

    @And("^create data type property with name \"([^\"]*)\"$")
    public void createDataTypeProperty(String property){
        CommonUtils.getClickableLink(CommonUtils.Sections.PROPERTIES, PROPERTIES_SECTION).click();
        CommonUtils.getLabelWithName("name", DataTypeUtils.getDataTypesRoot()).setValue(property);
        CommonUtils.getButtonWithText("Add", DataTypeUtils.getDataTypesRoot()).click();
    }

    @And("^set description \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setDescriptionForProperty(String description, String property) {
        DataTypeUtils.openPropertyDescription(property);
        OperationUtils.clickToEditDescriptionTextArea(PROPERTIES_SECTION);
        PROPERTIES_SECTION.$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", PROPERTIES_SECTION)
                .click();
    }

    @And("^set type \"([^\"]*)\" formatted as \"([^\"]*)\" for property \"([^\"]*)\"$")
    public void setTypeFormattedAsForProperty(String type, String as, String property) {
        DataTypeUtils.openPropertyTypes(property);
        DataTypeUtils.setPropertyType(type);
        DataTypeUtils.setFormattedAs(as);
    }

    @And("^set data type description \"([^\"]*)\"$")
    public void setDataTypeDescription(String description) {
        DataTypeUtils.clickToEditDescription(INFO_SECTION);
        INFO_SECTION.$("ace-editor textarea").sendKeys(description);
        CommonUtils.getButtonWithTitle("Save changes.", INFO_SECTION)
                .click();
    }

    @And("^set data type example \"([^\"]*)\"$")
    public void setDataTypeExample(String example) {
        DataTypeUtils.clickToEditDescription(EXAMPLE_SECTION);
        EXAMPLE_SECTION.$("ace-editor textarea").sendKeys(example);
        CommonUtils.getButtonWithTitle("Save changes.", EXAMPLE_SECTION)
                .click();
    }

    @And("^set property \"([^\"]*)\" as required \"([^\"]*)\"$")
    public void setPropertyAsRequired(String property, Boolean isRequired) {
        DataTypeUtils.openPropertyTypes(property);
        DataTypeUtils.setPropertyAsRequired(isRequired);
    }
}
