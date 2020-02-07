package apicurito.tests.steps;

import static org.assertj.core.api.Assertions.assertThat;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import apicurito.tests.utils.slenide.*;
import org.assertj.core.api.Condition;
import org.openqa.selenium.By;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.io.File;
import java.util.List;

import apicurito.tests.configuration.Component;
import apicurito.tests.configuration.TestConfiguration;
import apicurito.tests.configuration.templates.ApicuritoTemplate;
import apicurito.tests.utils.openshift.OpenShiftUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


@Slf4j
public class CommonSteps {

    @Given("^log into apicurito$")
    public void login() {
        Selenide.open(TestConfiguration.apicuritoUrl());
    }

    @When("^create a new API version \"([^\"]*)\"$")
    public void createANewApi(String version) {
        if ("3".equals(version)) {
            CommonUtils.getButtonWithText("New API", CommonUtils.getAppRoot()).shouldBe(visible, enabled).shouldNotHave(attribute("disabled"))
                    .click();
        } else {

            CommonUtils.getButtonWithText("Toggle Dropdown", CommonUtils.getAppRoot()).shouldBe(visible, enabled).shouldNotHave(attribute("disabled"))
                    .click();

            CommonUtils.getAppRoot().$$("a").filter(text("New (OpenAPI 2)")).first().click();
        }
    }

    @Then("^sleep for (\\d+) seconds$")
    public void sleepFor(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^import API \"([^\"]*)\"$")
    public void importAPI(String pathtoFile) {
        ImportExportUtils.importAPI(new File(pathtoFile));
    }

    @Then("^save API as \"([^\"]*)\" and close editor$")
    public void saveAPIAsAndCloseEditor(String format) {
        File exportedIntegrationFile = ImportExportUtils.exportAPIUtil(format);
        assertThat(exportedIntegrationFile)
                .exists()
                .isFile()
                .has(new Condition<>(f -> f.length() > 0, "File size should be greater than 0"));

        CommonUtils.getButtonWithText("Close", CommonUtils.getAppRoot())
                .click();
        CommonUtils.getButtonWithText("Don't Save", CommonUtils.getAppRoot())
                .click();
    }

    @Then("^delete API \"([^\"]*)\"$")
    public void deleteAPI(String file) {
        new File(file).delete();
    }

    @When("check that apicurito has {int} pods")
    public void checkThatApicuritoHasPods(int count) {
        List<Pod> pods = OpenShiftUtils.getInstance().getPods();
        int counter = 0;
        for (Pod pod : pods) {
            if (pod.getMetadata().getName().contains(Component.SERVICE.getName()) && pod.getStatus().getPhase().equals("Running")) {
                ++counter;
            }
        }
        assertThat(count).as("Apicurito should have %s pods but currently run %s", count, counter).isEqualTo(counter);
    }

    @When("deploy another custom resource")
    public void deployAnotherCustomResource() {
        String cr = "https://gist.githubusercontent.com/mmajerni/e47e14f2a1c2bf934219cb3d4508e81c/raw/ff59f25b5a37918f19c69d70931154c081b683fa/operatorUpdateTest.yaml";
        ApicuritoTemplate.deployCr(cr);

        //Wait for Rollout until there is no unavailable pod
        Integer tmp = Integer.MAX_VALUE;
        while (tmp != null) {
            //Wait for 5 seconds
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tmp = OpenShiftUtils.getInstance().apps().deployments().inNamespace(TestConfiguration.openShiftNamespace()).list().getItems().get(1).getStatus().getUnavailableReplicas();
        }

        //Wait another 15 seconds because of termination running pods
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("check that apicurito image is {string}")
    public void checkThatApicuritoImageIs(String image) {
        List<Pod> pods = OpenShiftUtils.getInstance().getPods();
        String imageName = "";
        for (Pod pod : pods) {
            if (pod.getMetadata().getName().contains(Component.SERVICE.getName())) {
                imageName = pod.getSpec().getContainers().get(0).getImage();
                break;
            }
        }
        assertThat(imageName).as("Apicurito has not container from %s", image).isEqualTo(image);
    }

    /*
        Create parameters or RDFs (Request data forms) on specified page
        param could be header|query|RFD
        page could be path|operations
     */
    @When("create {string} on {string} page with plus sign {string}")
    public void createParameterOnPage(String param, String page, String isPlus, DataTable table) {
        SelenideElement pageElement = page.equals("operations") ? OperationUtils.getOperationRoot() : PathUtils.getPathPageRoot();
        String aName = null;
        By section = CommonUtils.getSectionBy(param);

        switch (param) {
            case "query":
                aName = CommonUtils.Sections.QUERY_PARAM.getA();
                break;
            case "header":
                aName = CommonUtils.Sections.HEADER_PARAM.getA();
                break;
            case "RDF":
                aName = CommonUtils.Sections.RFD_PARAM.getA();
                break;
            case "cookie":
                aName = CommonUtils.Sections.COOKIE_PARAM.getA();
                break;
        }
        CommonUtils.openCollapsedSection(pageElement, section);

        if (Boolean.valueOf(isPlus)) {
            pageElement.$(section).$$("icon-button").filter(attribute("type", "add"))
                    .shouldHaveSize(1).first().shouldBe(visible).$("button").click();
        } else {
            pageElement.$(section).$$("a").filter(text(aName)).shouldHaveSize(1).first().shouldBe(visible).click();
        }
        CommonUtils.fillEntityEditorForm(table);
    }

    @When("^(set|check) type of \"([^\"]*)\" media type (?:to|is) \"([^\"]*)\" on property \"([^\"]*)\" for response \"([^\"]*)\"$")
    public void setTypeOfMediaType(String check, String mediaType, String type, String property, String response) {
        OperationUtils.ensureMediaTypeExistsForResponse(mediaType, response);
        SelenideElement mediaRow = $$("media-type-row").find(text(mediaType));
        SelenideElement typeElement = mediaRow.$(".type");
        if (!typeElement.has(cssClass("selected"))) {
            typeElement.click();
        }
        String buttonId = CommonUtils.getButtonId(property);
        if ("set".equalsIgnoreCase(check)) {
            CommonUtils.setDropDownValue(buttonId, type, $("schema-type-editor"));
        } else {
            String value = mediaRow.$(buttonId).getText();
            assertThat(value).as("%s is %s but should be %s", property, value, type).isEqualTo(type);
        }
    }

    @When("create a server on {string} page")
    public void createAServerOnPage(String page, DataTable table) {
        By section = CommonUtils.getSectionBy("server");
        SelenideElement pageElement = CommonUtils.getPageElement(page);
        CommonUtils.openCollapsedSection(pageElement, section);

        for (List<String> dataRow : table.cells()) {

            //open editor by plus button(true) or by link (false)
            if (Boolean.valueOf(dataRow.get(2))) {
                pageElement.$(section).$$("icon-button").filter(attribute("type", "add"))
                        .shouldHaveSize(1).first().shouldBe(visible).$("button").click();
            } else {
                pageElement.$(section).$$("a").filter(text("Add a server")).shouldHaveSize(1).first().shouldBe(visible).click();
            }

            //set server name and click Apply
            SelenideElement editorPage = MainPageUtils.getMainPageRoot().$("#entity-editor-form");
            editorPage.$("#serverUrl").sendKeys(dataRow.get(0));
            CommonUtils.getButtonWithText("Apply", editorPage).click();

            if (!dataRow.get(1).isEmpty()) {
                editorPage.$("#description").sendKeys(dataRow.get(1));
            }

            CommonUtils.getButtonWithText("Save", editorPage).click();
        }
    }

    @When("configure server variables for {string} on {string} page")
    public void configureServerVariablesForOnPage(String url, String page, DataTable table) {
        By section = CommonUtils.getSectionBy("server");
        SelenideElement pageElement = CommonUtils.getPageElement(page);
        CommonUtils.openCollapsedSection(pageElement, section);
        SelenideElement server = pageElement.$(section).$$(By.className("url")).filter((text(url))).first().parent();
        CommonUtils.getKebabButtonOnElement(server).click();
        CommonUtils.getDropdownMenuItem("Edit").click();

        SelenideElement variablesArea = MainPageUtils.getMainPageRoot().$("#entity-editor-form").$(By.className("server-variables"));
        for (List<String> dataRow : table.cells()) {
            //select right variable
            variablesArea.$$("li").filter(text(dataRow.get(0))).first().click();

            CommonUtils.getLabelWithType("text", variablesArea.$$(By.className("panel-body")).filter(visible).first()).sendKeys(dataRow.get(1));

            variablesArea.$$("textarea").filter(visible).first().sendKeys(dataRow.get(2));


        }
        CommonUtils.getButtonWithText("Save", MainPageUtils.getMainPageRoot().$("#entity-editor-form")).click();
    }
}
