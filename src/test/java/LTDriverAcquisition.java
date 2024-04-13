package com.lambdatest.ltOptions_w3c;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Assertions;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LTDriverAcquisition {

    String username = System.getenv("LT_USERNAME") == null ? "LT_USERNAME" : System.getenv("LT_USERNAME"); //Enter the Username here
    String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "LT_ACCESS_KEY" : System.getenv("LT_ACCESS_KEY"); //Enter the Access key here
    public String grid_url = System.getenv("LT_GRID_URL") == null ? "mobile-hub.lambdatest.com" : System.getenv("LT_GRID_URL");
    public String status = "passed";

    public static RemoteWebDriver driver = null;

    @Before
    public void setUp() throws Exception {

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });

        DesiredCapabilities capabilities = new DesiredCapabilities();

        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("w3c", true);
        ltOptions.put("console", false);
        ltOptions.put("network", false);
        ltOptions.put("visual", false);
        ltOptions.put("enableCustomTranslation", false);
        ltOptions.put("platformName", "ios");
        ltOptions.put("deviceName", "iPhone 14");
        ltOptions.put("platformVersion", "16.0");
        ltOptions.put("deviceOrientation", "PORTRAIT");
        ltOptions.put("build", "JUNIT_lt:options_w3c");
        ltOptions.put("name", "ios_lt:options_w3c");
        ltOptions.put("isRealMobile", false);
        capabilities.setCapability("lt:options", ltOptions);

        // Start the timer
        long tStart = System.currentTimeMillis();

        driver = new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@" + grid_url + "/wd/hub"), capabilities);

        long tEnd = System.currentTimeMillis();
        logger.info("Successfully acquired LT Session in " + ((tEnd - tStart)/1000) + " seconds");
    }

    @Test
    public void testSimple() throws Exception {
        try {

            driver.get("https://apple.com/iphone");

            status = "passed";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            status = "failed";
        }
    }

    @After
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.executeScript("lambda-status=" + status);
            driver.quit();
        }
    }

}
