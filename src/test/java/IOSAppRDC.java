import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import java.net.URL;

import java.net.MalformedURLException;

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

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:app", "storage:filename=ChaseiPhone_release-4.420-20230605_595_39646_debug-unprotected-chase.ipa");
        //caps.setCapability("appium:app", "storage:filename=Calculator.ipa");
        caps.setCapability("appium:deviceName", "iPhone_11_15_real_us");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appiumVersion", "2.0.0-beta56");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        sauceOptions.setCapability("build", "2023-05-27 - Chase RDC App Test Runs");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        driver = new IOSDriver(url, caps);

    }

    @DisplayName("iOS App Test - Chase - Real Device - Run 5")
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