import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DesktopSafariSelenium {

    private RemoteWebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setup() throws MalformedURLException {
        SafariOptions caps = new SafariOptions();
        caps.setCapability("platformName", "macOS 14");
        caps.setCapability("browserVersion", "17");
        caps.setCapability("browserName", "Safari");

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("armRequired", true);
        sauceOptions.put("build", "The Internet");
        sauceOptions.put("name", "Test Login Flow");
        //sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        //sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("username", "ShubhaG");
        sauceOptions.put("accessKey", "11d27d35-6f24-40a4-80fb-6f8463d91de5");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(url, caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void testLoginFlow() {  // Removed unnecessary throws
        driver.get("https://the-internet.herokuapp.com/login");

        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        username.sendKeys("tomsmith");

        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("SuperSecretPassword!");

        //WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#login")));
        WebElement loginBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/form/button/i")));
        loginBtn.click();

        WebElement banner = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        assertTrue(banner.isDisplayed() && banner.isEnabled(), "The 'flash' element should be visible and enabled.");

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}