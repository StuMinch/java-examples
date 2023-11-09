import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesktopFirefoxWeb {

    private RemoteWebDriver driver;

    @BeforeEach
    public void setup() throws MalformedURLException {

        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        FirefoxOptions options = new FirefoxOptions();
        options.setPlatformName("Windows 10");
        options.setBrowserVersion("117");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("name", "Firefox Desktop Web");
        sauceOptions.put("seleniumVersion", "latest");
        options.setCapability("sauce:options", sauceOptions);

        try {
            URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
            driver = new RemoteWebDriver(url, options);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBrowseToMozillaOrg() {
        driver.navigate().to("https://www.mozilla.org/");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
