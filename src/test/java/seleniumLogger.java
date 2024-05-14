import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class seleniumLogger {
    public static void main(String[] args) {
        // Setup Logger
        // https://www.selenium.dev/documentation/webdriver/troubleshooting/logging/
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINE);
        Arrays.stream(logger.getHandlers()).forEach(handler -> {
            handler.setLevel(Level.FINE);
        });
    }
}
