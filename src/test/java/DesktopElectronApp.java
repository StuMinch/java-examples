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

public class DesktopElectronApp {

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
        caps.setCapability("browserVersion", "31");
        caps.setCapability("browserName", "electron");
        caps.setBinary("td-electron-win32-x64\\td-electron.exe");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("build", "Desktop Electron");
        sauceOptions.put("name", "My Electron App");
        sauceOptions.put("app", "storage:1e6f6c3e-5113-4407-bc33-50e45d26f424");
        sauceOptions.put("username", "sminch");
        sauceOptions.put("accessKey", "29a183ea-4c7b-4c5d-8a62-099a764f7542");
        caps.setCapability("sauce:options", sauceOptions);


        // Acquire driver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");

        // Okta repro steps
        RemoteWebDriver driver = new RemoteWebDriver(url, caps);
        assert driver.findElement(By.className("title")).isDisplayed();

        driver.quit();
    }

}
