import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.lang.model.element.Element;
import java.net.URL;

import java.net.MalformedURLException;

public class IOSAppSim {
    private IOSDriver driver;

    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void IOSSetup(TestInfo testInfo) throws MalformedURLException {

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:app", "storage:filename=Retail_release-4.420-20230605_693_39703_automation-chase.app.zip");
        caps.setCapability("appium:deviceName", "iPhone 13 Simulator");
        caps.setCapability("appium:platformVersion", "15.5");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appiumVersion", "2.0.0-beta44");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        sauceOptions.setCapability("build", "IM-600");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        driver = new IOSDriver(url, caps);

    }
    @DisplayName("Retail_release-4.420-20230605_693_39703_automation-chase")
    @Test
    public void iOSAppTest() throws InterruptedException {
        driver.findElement(By.name("Allow Once")).click();
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