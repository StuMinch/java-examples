import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class runSauceLabs {

    private static RemoteWebDriver driver;

    public static void main(String[] args) throws Exception {

        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        // Create a map of capabilities
        ChromeOptions caps = new ChromeOptions();
        caps.setCapability("platformName", "Windows 11");
        caps.setCapability("browserVersion", "latest");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("build", "UnreachableBrowserException");
        sauceOptions.put("name", "ReproducingUnreachableBrowserException");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        caps.setCapability("sauce:options", sauceOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        // Okta repro steps
        Instant start = Instant.now();
        RemoteWebDriver driver = new RemoteWebDriver(url, caps);
        driver.navigate().to("https://www.google.com");

        Thread.sleep(5000);
        for (Instant now = Instant.now(); now.isBefore(start.plusSeconds(200)); now = Instant.now()) {
            WebElement query = driver.findElement(By.cssSelector("div[class='FPdoLc lJ9FBc'] input[name='btnK']"));
            query.click();
        }

        driver.quit();
    }

}
