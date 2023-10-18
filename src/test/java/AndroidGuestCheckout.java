import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

import java.net.MalformedURLException;
import java.time.Duration;

public class AndroidGuestCheckout {

    private AndroidDriver driver;

    /**
     * A Test Watcher is needed to be able to get the results of a Test so that it can be sent to Sauce Labs.
     * Note that the name is never actually used
     */
    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void AndroidSetup(TestInfo testInfo) throws MalformedURLException {

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:app", "storage:filename=home-depot-app.apk");
        caps.setCapability("appium:deviceName", "Android GoogleAPI Emulator");
        // Comment out the line above (Emulator) before using the line below (Real Device)
        //caps.setCapability("appium:deviceName", "Google.*");
        caps.setCapability("enableMultiWindows", true);
        caps.setCapability("appium:platformVersion", "12");
        caps.setCapability("appium:automationName", "UiAutomator2");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("name", testInfo.getDisplayName());
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new AndroidDriver(url, caps);
        System.out.println("https://app.saucelabs.com/tests/" + driver.getSessionId());

    }

    @DisplayName("Android Guest Checkout")
    @Test
    public void guestCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.findElement(AppiumBy.xpath("//android.widget.Button[@text='PROD']")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Allow']")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.Button[@text='While using the app']")).click();

        //driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Enter City or Postal Code']")).click();
        WebElement zipCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='Enter City or Postal Code']")));
        zipCode.click();


        driver.findElement(AppiumBy.xpath("//android.widget.EditText")).sendKeys("7001");
        driver.executeScript("mobile:performEditorAction", ImmutableMap.of("action", "done"));
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[contains(@text,'#')]")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='No, Thanks']")).click();
        driver.findElement(AppiumBy.xpath("//android.widget.Button[@text='OK']")).click();

        //driver.findElement(AppiumBy.xpath("android.widget.RelativeLayout")).click();
        WebElement layoutScreen = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.RelativeLayout")));
        layoutScreen.click();

        WebDriverWait waitHelp = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement canWeHelp = waitHelp.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.EditText[@text='What can we help you find?']")));
        canWeHelp.click();
        canWeHelp.sendKeys("1001567222");

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
