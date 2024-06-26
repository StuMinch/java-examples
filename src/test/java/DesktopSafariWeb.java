
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.runners.ParentRunner;

public class DesktopSafariWeb {

    private static String generateUuid() {
        // Generate a random UUID
        String uuidStr = "";
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            uuidStr += String.valueOf(random.nextInt(15));
        }
        return uuidStr;
    }

    public static void main(String[] args) throws Exception {

        // Generate a random UUID
        String localSessionUuid = generateUuid();

        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        // Create a map of capabilities
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "macOS 13");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("browserVersion", "16");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("uuid", localSessionUuid);
        sauceOptions.setCapability("build", "Infrastructure Error Troubleshooting");
        sauceOptions.setCapability("name", "Desktop Safari Web");
        sauceOptions.setCapability("commandTimeout", "480");
        sauceOptions.setCapability("idleTimeout", "600");
        sauceOptions.setCapability("uuid", localSessionUuid);
        sauceOptions.setCapability("maxDuration", "1800");
        sauceOptions.setCapability("recordScreenshots", "false");
        sauceOptions.setCapability("extendedDebugging", "false");
        sauceOptions.setCapability("timezone", "");
        sauceOptions.setCapability("screenResolution", "1600x1200");
        sauceOptions.setCapability("appiumVersion", "");
        sauceOptions.setCapability("capturePerformance", "false");
        sauceOptions.setCapability("seleniumVersion", "4.0.0");
        caps.setCapability("sauce:options", sauceOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        RemoteWebDriver driver;
        try {
            driver =  new RemoteWebDriver(url, caps);
            // Stop the timer
            long tEnd = System.currentTimeMillis();

            // Log troubleshooting info
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
        } catch (Exception e) {
            // Capture the error
            Logger exceptionLogger = Logger.getLogger("");
            exceptionLogger.log(Level.SEVERE, "Error acquiring driver", e);

            // Exit the program
            System.exit(1);
        }
    }
}