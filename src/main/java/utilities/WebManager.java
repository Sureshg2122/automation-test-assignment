package utilities;

// using webdriver manager eliminates the need to have driver files

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebManager {

    // enum to restrict the browser parameter input. Can add additional browsers
    public enum BrowserType {
        CHROME
    }

    /**
     * setWebDriver accepts enum BrowserType and based on the browser type initiates the webdriver and returns the requested browser driver
     *
     * @param browser enum BrowserType
     * @return WebDriver
     */
    public WebDriver setWebDriver(String browser) {

        switch (browser) {

            case "CHROME":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();

            default:
                throw new IllegalArgumentException();

        }
    }

}
