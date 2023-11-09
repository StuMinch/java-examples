import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import java.net.URL;

import java.net.MalformedURLException;

public class AndroidApp {
    private AndroidDriver driver;

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
        caps.setCapability("appium:app", "storage:filename=cart-release_v23.104.302-20001_arm64-v8a.apk");  // The filename of the mobile app
        caps.setCapability("appium:deviceName", "Android GoogleAPI Emulator");
        caps.setCapability("appium:platformVersion", "10.0");
        caps.setCapability("appium:automationName", "UiAutomator2");
        //caps.setCapability("prerun", "filename=adb-prerun.sh");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("appiumVersion", "2.0.0");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("build", "Instacart Emulator POV");
        sauceOptions.setCapability("name", "Caper app on an emulator");
        sauceOptions.setCapability("deviceOrientation", "LANDSCAPE");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        AndroidDriver driver = new AndroidDriver(url, caps);

    }

    @Test
    public void androidWebTest() throws InterruptedException {
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