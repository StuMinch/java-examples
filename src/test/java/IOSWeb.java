import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import java.net.URL;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.net.MalformedURLException;
import java.time.Duration;

public class IOSWeb {
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
        caps.setCapability("browserName", "Safari");
        caps.setCapability("appium:deviceName", "iPhone 15 Simulator");
        // Comment out the line above (Emulator) before using the line below (Real Device)
        //caps.setCapability("appium:deviceName", "iPhone.*");
        caps.setCapability("appium:platformVersion", "17.0");
        caps.setCapability("appium:automationName", "XCUITest");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("tunnelName", "sauce-connect-v4");
        sauceOptions.setCapability("build", "Tunnel Version Check");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        driver = new IOSDriver(url, caps);

    }

    /*
    @DisplayName("iOS Web Test")
    @Test
    public void iOSWebTest() throws InterruptedException {
        driver.get("https://chase.com");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        //Assertions.assertEquals("iPhone - Apple", driver.getTitle());
    }
    */
    @DisplayName("iOS Web Test on iOS 17")
    @Test
    public void iOSWebTest() throws MalformedURLException, InterruptedException {
        driver.get("https://saucelabs.com");

        int seconds = 10;
        Duration duration = Duration.ofSeconds(seconds);

        WebDriverWait wait = new WebDriverWait(driver, duration);
        ExpectedCondition<Boolean> titleIsCorrect = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return d.getTitle().equals("Sauce Labs: Cross Browser Testing, Selenium Testing & Mobile Testing");
            }
        };

        wait.until(titleIsCorrect);

        Assertions.assertEquals("Sauce Labs: Cross Browser Testing, Selenium Testing & Mobile Testing", driver.getTitle());
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