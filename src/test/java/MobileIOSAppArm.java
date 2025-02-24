import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class MobileIOSAppArm {

    private IOSDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() throws MalformedURLException {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:app", "storage:filename=Retail_release-2025.01-Minor1_13_44420_automation-chase.app.zip");  // The filename of the mobile app
        caps.setCapability("appium:deviceName", "iPad Simulator");
        caps.setCapability("appium:platformVersion", "18.0");
        caps.setCapability("appium:autoAcceptAlerts", false);
        caps.setCapability("appium:automationName", "XCUITest");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("armRequired", true);
        sauceOptions.setCapability("name", "iPad Simulator - Handle Geolocation Prompt");
        sauceOptions.setCapability("deviceOrientation", "PORTRAIT");
        sauceOptions.setCapability("appiumVersion", "2.11.3");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        IOSDriver driver = new IOSDriver(url, caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testTappingAllowOncePrompt() {

        driver.getPageSource();

        driver.findElement(By.name("Allow Once")).click();

        WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Sign in")));
        signInButton.click();

        driver.quit();
    }
}