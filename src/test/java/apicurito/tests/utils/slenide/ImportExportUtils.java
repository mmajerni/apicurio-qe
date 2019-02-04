package apicurito.tests.utils.slenide;

import apicurito.tests.configuration.CustomWebDriverProvider;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.io.File;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ImportExportUtils {

    public static File exportAPIUtil(String format) throws InterruptedException {

        CommonUtils.getAppRoot().$$("button")
                .filter(attribute("class","btn btn-lg btn-primary dropdown-toggle btn-save" )).first().click();
        CommonUtils.getAppRoot().$$("a").filter(text("Save as " + format.toUpperCase())).first().click();

        String filePath = CustomWebDriverProvider.DOWNLOAD_DIR + File.separator + "openapi-spec." + format.toLowerCase();

        // wait for download
        Thread.sleep(5000);

        return new File(filePath);
    }

    public static void importAPI(File file) {
        CommonUtils.getAppRoot().$("#load-file").uploadFile(file);
    }
}
