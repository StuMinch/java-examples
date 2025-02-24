import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MobileChromeAppium {

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() throws MalformedURLException {
        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        // Create a map of capabilities
        UiAutomator2Options caps = new UiAutomator2Options();
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserName", "chrome");
        caps.setCapability("appium:deviceName", "Google.*");
        caps.setCapability("appium:automationName", "UiAutomator2");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("build", "File Upload Use Case");
        sauceOptions.put("name", "File Upload on Android Chrome");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("appiumVersion", "stable");
        caps.setCapability("sauce:options", sauceOptions);

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new AndroidDriver(url, caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    }

    @Test
    public void testFileUpload() throws InterruptedException, IOException {
        driver.get("https://the-internet.herokuapp.com/upload");


        WebElement chooseFile = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("file-upload")));
        Thread.sleep(2000);
        chooseFile.click();

        WebElement uploadBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("file-submit")));
        Thread.sleep(2000);
        uploadBtn.click();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}