import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import java.net.URL;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOSAppRDC {
    private IOSDriver driver;

    /**
     * A Test Watcher is needed to be able to get the results of a Test so that it can be sent to Sauce Labs.
     * Note that the name is never actually used
     */
    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void IOSSetup(TestInfo testInfo) throws MalformedURLException {

        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("platformVersion", "16");
        caps.setCapability("appium:deviceName", "iPhone.*");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appiumVersion", "2.0.0");
        caps.setCapability("appium:app", "storage:c0893b23-73bd-4582-8945-ff8ab3f39557");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        sauceOptions.setCapability("build", "US-East-4 Connectivity Test");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        //URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        URL url = new URL("https://ondemand.us-east-4.saucelabs.com/wd/hub");

        driver = new IOSDriver(url, caps);

    }

    @DisplayName("Chase - Launch App")
    @Test
    public void iOSAppTest() throws InterruptedException {
        driver.getPageSource();
    }

    /**
     * Custom TestWatcher for Sauce Labs projects.
     */

    public class SauceTestWatcher implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            driver.executeScript("sauce:job-result=passed");
            driver.quit();
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            driver.executeScript("sauce:job-result=failed");
            driver.quit();
        }
    }

}