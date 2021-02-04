package utilities;

import com.assignment.app.pageobjectfactory.LoginPage;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;

public class NavigationUtils {

    public void getToLogin(WebDriver driver, Properties prop) {
        driver.get(prop.getProperty("ui-test-url"));

        LoginPage loginPage = new LoginPage(driver);

        if (loginPage.getIframes().size() > 0) {
            driver.switchTo().frame(loginPage.getLoginFrameId());

            try {
                new WebDriverWait(driver, 20)
                        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                        .until(ExpectedConditions.elementToBeClickable(loginPage.getCloseiFrame()));
            } catch (TimeoutException exception) {
                Assert.assertTrue(false, "page load not completed within 10 seconds: iframe Done button not clickable in 10 seconds");
            }

            loginPage.getCloseiFrame().click();

            driver.switchTo().defaultContent();

            new WebDriverWait(driver, 20)
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                    .until(ExpectedConditions.elementToBeClickable(loginPage.getUsername()));

        }
    }

    public void getToParentSupportSetup(WebDriver driver) throws InterruptedException {

        LoginPage loginPage = new LoginPage(driver);

        int tryCount = 0;
        int maxcount = 5;

        while (true) {
            try {
                loginPage.getSetUpParentSupport().click();
                break;
            } catch (ElementClickInterceptedException e) {
                try {
                    Boolean validatePage = loginPage.getCreateANewAccount().isDisplayed();
                    if (validatePage) {
                        break;
                    }
                } catch (Exception validate) {
                    if (tryCount++ == maxcount) {
                        throw e;
                    }
                }
            }

            Thread.sleep(1000l);
        }


        new WebDriverWait(driver, 10)
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(loginPage.getCreateANewAccount()));

        loginPage.getCreateANewAccount().click();


        new WebDriverWait(driver, 20)
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath("//*[contains(text(), 'Loading...')]"))));

    }
}
