import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverAcquisition {

    private static String generateUuid() {
        // Generate a random UUID
        String uuidStr = "";
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            uuidStr += String.valueOf(random.nextInt(15));
        }
        return uuidStr;
    }

    public static void logging() throws IOException {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINEST);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINEST);
        });
    }

    public static void main(String[] args) throws Exception {

        // Generate a random UUID
        String localSessionUuid = generateUuid();

        // Create a map of capabilities
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("appium:deviceName", "iPhone 11 Simulator");
        caps.setCapability("appium:deviceOrientation", "portrait");
        caps.setCapability("appium:platformVersion", "15.4");
        caps.setCapability("appium:automationName", "XCUITest");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("appiumVersion", "2.0.0");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("uuid", localSessionUuid);
        sauceOptions.setCapability("build", "Stuart M1 Tests");
        sauceOptions.setCapability("name", "Reproducing Grid Driver Acquistion Error - Java Edition");
        caps.setCapability("sauce:options", sauceOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        IOSDriver driver = new IOSDriver(url, caps);

        // Stop the timer
        long tEnd = System.currentTimeMillis();

        // Log results
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINEST);
        logger.info("Successfully acquired Sauce Session in " + ((tEnd - tStart)/1000) + " seconds");
        logger.info("Local UUID: " + localSessionUuid);
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
    }
}