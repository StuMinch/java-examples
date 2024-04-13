import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;

import java.net.URL;

import java.net.MalformedURLException;
import java.util.Random;

public class IOSAppSim {

    private static String generateUuid() {
        // Generate a random UUID
        String uuidStr = "";
        for (int i = 0; i < 12; i++) {
            Random random = new Random();
            uuidStr += String.valueOf(random.nextInt(15));
        }
        return uuidStr;
    }
    private IOSDriver driver;

    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void IOSSetup(TestInfo testInfo) throws MalformedURLException {

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:app", "storage:f2401caa-60c7-49fb-ae95-e47ea6854730");
        caps.setCapability("appium:deviceName", "iPhone 14 Simulator");
        caps.setCapability("appium:platformVersion", "16.2");
        caps.setCapability("appium:automationName", "XCUITest");
        caps.setCapability("appiumVersion", "2.1.3");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        //sauceOptions.setCapability("tunnelName", "composed-docker-sc");
        sauceOptions.setCapability("build", "Chase iOS Start Times");
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        driver = new IOSDriver(url, caps);

    }
    @DisplayName("Chase - Allow Once - Geolocation Prompt")
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