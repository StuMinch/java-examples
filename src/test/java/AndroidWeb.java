import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import java.net.URL;

import java.net.MalformedURLException;

public class AndroidWeb {
    private AndroidDriver androidDriver;

    /**
     * A Test Watcher is needed to be able to get the results of a Test so that it can be sent to Sauce Labs.
     * Note that the name is never actually used
     */
    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void AndroidSetup() throws MalformedURLException {

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("appium:deviceName", "Android GoogleAPI Emulator");
        // Comment out the line above (Emulator) before using the line below (Real Device)
        //caps.setCapability("appium:deviceName", "Google.*");
        caps.setCapability("appium:platformVersion", "12");
        caps.setCapability("appium:automationName", "UiAutomator2");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", "Android Web Test");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        androidDriver = new AndroidDriver(url, caps);

    }

    @Test
    public void androidWebTest() throws InterruptedException {
        androidDriver.get("https://android.com");
        Assertions.assertEquals("Android - Secure & Reliable Mobile Operating System", androidDriver.getTitle());
    }

    /**
     * Custom TestWatcher for Sauce Labs projects.
     */

    public class SauceTestWatcher implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            androidDriver.executeScript("sauce:job-result=passed");
            androidDriver.quit();
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            androidDriver.executeScript("sauce:job-result=failed");
            androidDriver.quit();
        }
    }

}