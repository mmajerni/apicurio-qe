package apicurito.tests.utils.slenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class DataTypeUtils {

    public static SelenideElement getDataTypesRoot() { return $("definition-form").shouldBe(visible); }

    public static void openPropertyDescription(String property){
        getDataTypesRoot().$$("property-row")
                .filter(text(property)).first()
                .$$("div").filter(attribute("class", "description"))
                .first()
                .click();
    }

    public static void openPropertyTypes(String property){
        ElementsCollection elements = getDataTypesRoot().$$("property-row")
                .filter(text(property)).first()
                .$$("div").filter(attribute("class", "summary"));
        if(elements.size() == 1) {
                elements.first().click();
        }
    }

    public static void setPropertyType(String type){
        SelenideElement response = getDataTypesRoot().$("#api-property-type");
        response.click();
        response.parent().$$("li").filter(text(type)).first().click();
    }

    public static void setFormattedAs(String as){
        SelenideElement response = getDataTypesRoot().$("#api-property-type-as");
        response.click();
        response.parent().$$("li").filter(text(as)).first().click();
    }

    public static void clickToEditDescription(SelenideElement section){ //TODO same in operation utils
        section.$$("div").filter(attribute("title", "Click to edit.")).first().click();
    }

    public static void setPropertyAsRequired(Boolean isRequired){
        getDataTypesRoot().$("#api-property-required").click();
        SelenideElement dropdown = getDataTypesRoot().$$("div").filter(attribute("class","dropdown open")).first().$("ul");
        if(isRequired){
            dropdown.$$("a").filter(matchesText("Required")).first().click();
        }else{
            dropdown.$$("a").filter(matchesText("Not Required")).first().click();
        }

    }
}
