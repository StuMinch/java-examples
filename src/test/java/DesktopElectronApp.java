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

public class DesktopElectronApp {

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
        caps.setCapability("platformName", "Windows 11");
        caps.setCapability("browserVersion", "33");
        caps.setCapability("browserName", "electron");
        //caps.addArguments("--remote-debugging-pipe");
        //caps.setBinary("db-ui-Setup-2503.20.1085\\db-ui-Setup-2503.20.1085\\db-ui-Setup-2503.20.1085.exe");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("build", "Electron Testing");
        sauceOptions.put("name", "Folder structure");
        sauceOptions.put("app", "storage:1bec7105-00f9-4a69-a026-ac6a05a364ca");
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
    public void testEnterUserDetails() throws InterruptedException, IOException {
        WebElement repId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("repId")));
        repId.sendKeys("ABC1");

        WebElement firstName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstName")));
        firstName.sendKeys("Stuart");

        WebElement lastName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lastName")));
        lastName.sendKeys("Minchington");

        driver.quit();
    }
}