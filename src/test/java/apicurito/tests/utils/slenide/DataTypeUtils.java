package apicurito.tests.utils.slenide;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTypeUtils {

    public static SelenideElement getDataTypesRoot() {
        return $("definition-form").shouldBe(visible);
    }

    public static void openPropertyDescription(String property) {
        log.info("Opening property description for property {}", property);

        getDataTypesRoot().$$("property-row")
                .filter(text(property)).first()
                .$$("div").filter(attribute("class", "description"))
                .first()
                .click();
    }

    public static void openPropertyTypes(String property) {
        log.info("Opening property types for property {}", property);

        ElementsCollection elements = getDataTypesRoot().$$("property-row")
                .filter(text(property)).first()
                .$$("div").filter(attribute("class", "summary"));
        if (elements.size() == 1) {
            elements.first().click();
        }
    }
}
