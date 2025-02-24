import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MobileChromeSelenium {

    private static RemoteWebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) throws Exception {

        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        // Create a map of capabilities
        ChromeOptions caps = new ChromeOptions();
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserVersion", "latest");
        caps.setCapability("browserName", "chrome");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("build", "File Upload Use Case");
        sauceOptions.put("name", "File Upload on Android Chrome");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);


        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Basic test - Main goal is to see if the Electron app loads
        RemoteWebDriver driver = new RemoteWebDriver(url, caps);

    }

    @Test
    public void testFileUpload() throws InterruptedException, IOException {
        driver.get("https://the-internet.herokuapp.com/upload");

        WebElement upload = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#file-upload")));
        upload.click();
        Thread.sleep(2000);
        upload.click();
        Thread.sleep(2000);
        upload.click();
        Thread.sleep(2000);
        upload.sendKeys();

        driver.quit();
    }
}