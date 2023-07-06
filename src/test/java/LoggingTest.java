package dev.selenium.troubleshooting;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LoggingTest {

    public RemoteWebDriver driver;

    @BeforeEach
    public void setup(TestInfo testInfo) throws MalformedURLException {

        FirefoxOptions browserOptions = new FirefoxOptions();
        browserOptions.setPlatformName("Windows 10");
        browserOptions.setBrowserVersion("latest");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("name", testInfo.getDisplayName());
        browserOptions.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        WebDriver driver = RemoteWebDriver.builder().oneOf(browserOptions).address(url).build();
    }

    @AfterEach
    public void loggingOff() {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.INFO);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.INFO);
        });
    }

    @DisplayName("Desktop Firefox Latest Web Test")
    @Test
    public void logging() throws IOException {
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        Handler handler = new FileHandler("selenium.xml");
        logger.addHandler(handler);

        //Logger.getLogger(RemoteWebDriver.class.getName()).setLevel(Level.FINEST);
        Logger.getLogger(SeleniumManager.class.getName()).setLevel(Level.SEVERE);
        /*
        Logger localLogger = Logger.getLogger(this.getClass().getName());
        localLogger.warning("this is a warning");
        localLogger.info("this is useful information");
        localLogger.fine("this is detailed debug information");

        byte[] bytes = Files.readAllBytes(Paths.get("selenium.xml"));
        String fileContent = new String(bytes);

        Assertions.assertTrue(fileContent.contains("this is a warning"));
        Assertions.assertTrue(fileContent.contains("this is useful information"));
        Assertions.assertTrue(fileContent.contains("this is detailed debug information"));
        */
        driver.navigate().to("https://www.saucedemo.com");
        Assertions.assertEquals("Swag Labs", driver.getTitle());
    }
}