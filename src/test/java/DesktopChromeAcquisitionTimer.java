import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DesktopChromeAcquisitionTimer {

    private static RemoteWebDriver driver;

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
        caps.setCapability("browserVersion", "latest");
        caps.addArguments("--incognito");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("build", "Grid Driver Acquisition Timing");
        sauceOptions.setCapability("name", "Desktop Chrome");
        caps.setCapability("sauce:options", sauceOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        try {
            driver = new RemoteWebDriver(url, caps);
            // Stop the timer
            long tEnd = System.currentTimeMillis();

            // Log troubleshooting info
            logger.info("********** Successfully acquired Sauce Session in " + ((tEnd - tStart)/1000) + " seconds **********");
            logger.info("Sauce UUID: " + driver.getSessionId());
            logger.info("Sauce URL: https://app.saucelabs.com/tests/" + driver.getSessionId());

            // Open the Sauce Demo website
            driver.get("https://saucedemo.com");

            // Enter the username and password
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");

            // Click the login button
            driver.findElement(By.id("login-button")).click();

            // Assert that the title element is displayed
            assert driver.findElement(By.className("title")).isDisplayed();

            // Set the job result to passed
            driver.executeScript("sauce:job-result=passed");

            // Quit the driver
            driver.quit();
        } catch (Exception e) {
            // Capture the error
            Logger exceptionLogger = Logger.getLogger("");
            exceptionLogger.log(Level.SEVERE, "Error acquiring driver", e);

            // Exit the program
            System.exit(1);
        }
    }

}
