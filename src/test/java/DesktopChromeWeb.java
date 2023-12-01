import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v114.performance.Performance;
import org.openqa.selenium.devtools.v114.performance.model.Metric;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Demo tests with Selenium.
 */
public class DesktopChromeWeb {
    public RemoteWebDriver driver;
    DevTools devTools;

    /**
     * A Test Watcher is needed to be able to get the results of a Test so that it can be sent to Sauce Labs.
     * Note that the name is never actually used
     */
    @RegisterExtension
    public SauceTestWatcher watcher = new SauceTestWatcher();

    @BeforeEach
    public void setup(TestInfo testInfo) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setPlatformName("Windows 11");
        options.setBrowserVersion("latest");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("name", testInfo.getDisplayName());
        sauceOptions.put("devTools", true);
        //sauceOptions.put("tunnelName", "composed-docker-sc");
        options.setCapability("sauce:options", sauceOptions);
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        driver = new RemoteWebDriver(url, options);
    }

    @DisplayName("Desktop Web Test")
    @Test
    public void desktopWebTest() {
        driver.navigate().to("https://www.saucedemo.com");
        Assertions.assertEquals("Swag Labs", driver.getTitle());
    }

    @DisplayName("Chrome Dev Tools")
    @Test
    public void performanceMetrics() {
        driver.get("https://www.selenium.dev/selenium/web/frameset.html");

        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Performance.enable(Optional.empty()));

        List<Metric> metricList = devTools.send(Performance.getMetrics());

        Map<String, Number> metrics = new HashMap<>();
        for (Metric metric : metricList) {
            metrics.put(metric.getName(), metric.getValue());
        }

        Assertions.assertTrue(metrics.get("DevToolsCommandDuration").doubleValue() > 0);
        Assertions.assertEquals(12, metrics.get("Frames").intValue());
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
