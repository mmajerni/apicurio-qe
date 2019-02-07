package apicurito.tests.utils.slenide;

import apicurito.tests.steps.PathSteps;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PathUtils {


    public static SelenideElement getPathPageRoot() { return $("path-form").shouldBe(visible); }

    public static SelenideElement getCreateOperationButton(PathSteps.Operations operation){
        log.info("searching for add operation button for operation {}", operation);

        ElementsCollection operations = getPathPageRoot().$$("div").filter(attribute("class","operation-tab " + operation.toString().toLowerCase() + "-tab"));
        if (operations.size() > 0){     //if size is 0, operation tab is already selected
            operations.first().click();
        }else{
            //Check that selected operation is ours $operation
            assertThat(getPathPageRoot().$$("div").filter(attribute("class","operation-tab " + operation.toString().toLowerCase() + "-tab selected")).size()).isEqualTo(1);
        }
        return CommonUtils.getButtonWithText("Add Operation", getPathPageRoot());
    }

    public static SelenideElement getOperationButton(PathSteps.Operations operation, SelenideElement differentRoot) {
        log.info("searching for operation button for operation {}", operation);

        return differentRoot.$(By.className(operation.toString().toLowerCase() + "-tab")).shouldBe(enabled);
    }

    public static void createPathParameter(String parameter){
        CommonUtils.getButtonWithText("Create",  getPathPageRoot().$$("path-param-row").filter(text(parameter)).first()).click();
    }

    public static void openPathDescription(String parameter){
        ElementsCollection elements = getPathPageRoot().$$("path-param-row")
                .filter(text(parameter)).first()
                .$$("div").filter(attribute("class", "description"));
        if(elements.size() == 1) {
            elements.first().click();
        }
    }

    public static void openPathTypes(String parameter){
        ElementsCollection elements = getPathPageRoot().$$("path-param-row")
                .filter(text(parameter)).first()
                .$$("div").filter(attribute("class", "summary"));
        if(elements.size() == 1) {
            elements.first().click();
        }
    }
}
