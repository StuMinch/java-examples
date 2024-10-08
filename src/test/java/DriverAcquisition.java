import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;


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
        caps.setCapability("platformName", "iOS");
        // ARM Build
        //caps.setCapability("appium:app", "storage:d996008f-bccb-473f-a2e5-78eefd8b5636");
        // Multi-arch Build
        caps.setCapability("appium:app", "storage:2bcbc75f-cc5c-4db5-b616-96295f8f6c97");
        //caps.setCapability("appium:deviceName", "iPhone 15 Simulator");
        caps.setCapability("appium:deviceName", "iPhone 14 Simulator");
        caps.setCapability("appium:deviceOrientation", "portrait");
        //caps.setCapability("appium:platformVersion", "17.0");
        caps.setCapability("appium:platformVersion", "16.2");
        caps.setCapability("appium:automationName", "XCUITest");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("appiumVersion", "2.0.0");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("uuid", localSessionUuid);
        //sauceOptions.setCapability("name", "iOS 17 ARM Simulator");
        sauceOptions.setCapability("name", "iOS 16.2 Simulator");
        //sauceOptions.setCapability("armRequired", true);
        caps.setCapability("sauce:options", sauceOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        IOSDriver driver;
        try {
            driver = new IOSDriver(url, caps);
            // Stop the timer
            long tEnd = System.currentTimeMillis();

            // Log troubleshooting info
            logger.info("Successfully acquired Sauce Session in " + ((tEnd - tStart)/1000) + " seconds");
            logger.info("Local UUID: " + localSessionUuid);
            logger.info("Sauce UUID: " + driver.getSessionId());
            logger.info("Sauce URL: https://app.saucelabs.com/tests/" + driver.getSessionId());

            driver.findElement(By.name("Allow Once")).click();

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