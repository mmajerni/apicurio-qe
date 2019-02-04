package apicurito.tests.utils.slenide;

import apicurito.tests.steps.PathSteps;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class PathUtils {


    public static SelenideElement getPathPageRoot() { return $("path-form").shouldBe(visible); }

    public static SelenideElement getCreateOperationButton(PathSteps.Operations operation, SelenideElement differentRoot){
        log.info("searching for create operation button for operation {}", operation);

        SelenideElement element = getPathPageRoot().$$("div").filter(attribute("class","api-operation empty")).find(text(operation.toString()));
        return element.findAll(By.tagName("button")).filter(Condition.exactText("Create Operation")).first();
    }

    public static SelenideElement getOperationButton(PathSteps.Operations operation, SelenideElement differentRoot) {
        log.info("searching for operation button for operation {}", operation);
        ElementsCollection operations = differentRoot.$$("div").filter(attribute("class", "api-operation"));

        ElementsCollection operationElements = null;
        for (SelenideElement op : operations){
            operationElements = op.$$("span").filter(Condition.matchText(operation.toString()));
            if ( operationElements.size() == 1 ){
                break;
            }
        }
        return operationElements.first();
    }

    public static void createPathParameter(String parameter){
        CommonUtils.getButtonWithText("Create",  getPathPageRoot().$$("path-param-row").filter(text(parameter)).first()).click();
    }

    public static void openPathDescription(String parameter){
        getPathPageRoot().$$("path-param-row")
                .filter(text(parameter)).first()
                .$$("div").filter(attribute("class", "description"))
                .first()
                .click();
    }

    public static void openPathTypes(String parameter){
        getPathPageRoot().$$("path-param-row")
                .filter(text(parameter)).first()
                .$$("div").filter(attribute("class", "summary"))
                .first()
                .click();
    }

    public static void setPathType(String parameter, String type){
        SelenideElement response = getPathPageRoot().$$("path-param-row").filter(text(parameter)).first()
                .$("#api-param-type");
        response.click();
        response.parent().$$("li").filter(text(type)).first().click();
    }

    public static void setFormattedAs(String parameter, String as){
        SelenideElement response = getPathPageRoot().$$("path-param-row").filter(text(parameter)).first()
                .$("#api-param-type-as");
        response.click();
        response.parent().$$("li").filter(text(as)).first().click();
    }
}
