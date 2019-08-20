package apicurito.tests.utils.slenide;

import apicurito.tests.configuration.CustomWebDriverProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;

@Slf4j
public class ImportExportUtils {

    public static File exportAPIUtil(String format) {

        CommonUtils.getAppRoot().$$("button")
                .filter(attribute("class", "btn btn-lg btn-primary dropdown-toggle btn-save")).first().click();
        CommonUtils.getAppRoot().$$("a").filter(text("Save as " + format.toUpperCase())).first().click();

        String filePath = CustomWebDriverProvider.DOWNLOAD_DIR + File.separator + "openapi-spec." + format.toLowerCase();

        // wait for download
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exception) {
            log.warn("Wait for download failed. File does not have to be downloaded.");
        }
        return new File(filePath);
    }

    public static void importAPI(File file) {
        CommonUtils.getAppRoot().$("#load-file").uploadFile(file);
    }
}
