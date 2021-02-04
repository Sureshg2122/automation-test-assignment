package com.assignment.app;

import com.assignment.app.pageobjectfactory.LoginPage;
import com.assignment.app.pageobjectfactory.ParentSupportAccountCreationPage;
import com.assignment.app.utilities.NavigationUtils;
import com.assignment.app.utilities.OtherUtils;
import com.assignment.app.utilities.WebManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UiTest {

    WebDriver driver;
    InputStream propertiesFile;
    Properties prop = new Properties();

    @BeforeClass
    public void setPropertiesFile() throws IOException {
        // load properties file from resources
        propertiesFile = getClass().getClassLoader().getResourceAsStream("test.properties");
        if (propertiesFile != null) {
            prop.load(propertiesFile);
        } else {
            throw new FileNotFoundException("Property file test.properties not found in the classpath");
        }
    }

    @BeforeMethod
    public void setBrowser() throws Exception {

        // verify if the parameter contains one of the expected browser types else throw exception
        if (Arrays.stream(WebManager.BrowserType.values()).anyMatch((t) -> t.name().equals(System.getProperty("browser").toUpperCase()))) {
            driver = new WebManager().setWebDriver(System.getProperty("browser").toUpperCase());
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().window().maximize();
        } else {
            throw new Exception("browser system property should be a value in " + Arrays.toString(WebManager.BrowserType.values()));
        }
    }

    // UI Test 1. Ensure this website loads: https://www.mypedia.pearson.com/

    // Solution: 1. Open Website 2. if a pop up shows up and the Done button is clickable then the page has loaded.
    // 3. Else (if no popup) verify if username field is clickable to ensure page load
    @Test
    public void verifyIfPageLoads() {
        driver.get(prop.getProperty("ui-test-url"));

        LoginPage loginPage = new LoginPage(driver);

        if (loginPage.getIframes().size() > 0) {

            // if introducing parent accounts iframe exists, then wait until Done button is clickable to validate page load
            driver.switchTo().frame(loginPage.getLoginFrameId());
            try {
                new WebDriverWait(driver, 20)
                        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                        .until(ExpectedConditions.elementToBeClickable(loginPage.getCloseiFrame()));
            } catch (TimeoutException exception) {
                if (!loginPage.getCloseiFrameUsingNext().isDisplayed()) {
                    Assert.assertTrue(false, "page load not completed within 10 seconds: iframe Done button not clickable in 10 seconds");
                }
            }
        } else {

            // if introducing parent accounts iframe does not exists (might happen due to cookies), then wait until username is clickable
            try {
                new WebDriverWait(driver, 10)
                        .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                        .until(ExpectedConditions.elementToBeClickable(loginPage.getUsername()));
            } catch (TimeoutException exception) {
                Assert.assertTrue(false, "page load not completed within 10 seconds: username element not clickable in 10 seconds");
            }
        }

    }

    // Ui Test 2. Confirm that language dropdown has at least 3 languages

    // Solution 1. Open the language drop down and get a count of number of menu items in the drop down

    @Test
    public void verifyNumberOfLanguages() throws InterruptedException {

        new NavigationUtils().getToLogin(driver, prop);

        LoginPage loginPage = new LoginPage(driver);

        // retry to avoid element click interception

        int tryCount = 0;
        int maxcount = 5;

        while (true) {
            try {
                loginPage.getLanguageDropDown().click();
                break;
            } catch (ElementClickInterceptedException e) {
                if (tryCount++ == maxcount) {
                    throw e;
                }
            }
            Thread.sleep(1000l);
        }

        // wait until language menu is opened

        new WebDriverWait(driver, 10)
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(ExpectedConditions.elementToBeClickable(loginPage.getLanguageMenu()));

        Assert.assertTrue(loginPage.getLanguageOptions().size() >= 3);
    }

    // Ui Test 3. Auto-Select different languages and validate that the label of the [CONTINUE] button changes to selected language.

    // Solution: 1.Open the language drop down and loop through the language options available.
    // 2. Get the text from menu item to find out the language
    // 3. language.json file in the resources folder contains a map of language and the translation for continue in that language
    // 4. Unicode characters are used
    // 5. From the language.json file the label expected for continue button is received
    // 6. The language is selected in the language drop down
    // 7. From Step 5 verify if label for continue is updated based on the language selected
    @Test
    public void verifyLanguageSelection() throws IOException, ParseException, InterruptedException {
        new NavigationUtils().getToLogin(driver, prop);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.getLanguageDropDown().click();

        new WebDriverWait(driver, 10)
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(ExpectedConditions.elementToBeClickable(loginPage.getLanguageMenu()));

        FileReader reader = new FileReader(getClass().getClassLoader().getResource("language.json").getFile());

        if (reader == null) {
            throw new FileNotFoundException("api data file language.json not found in the classpath");
        }


        JSONParser jsonParser = new JSONParser();

        Object obj = jsonParser.parse(reader);

        JSONArray jsonArray = (JSONArray) obj;

        for (int i = 0; i < loginPage.getLanguageOptions().size(); i++) {

            for (Object jsonObject : jsonArray) {
                if (((JSONObject) jsonObject).get("language").equals(loginPage.languageOptionSelector(i + 1).getText())) {
                    loginPage.languageOptionSelector(i + 1).click();
                    Thread.sleep(2000l);
                    Assert.assertTrue(((JSONObject) jsonObject).get("continueinlanguage").equals(loginPage.getContinueButton().getText()));
                    loginPage.getLanguageDropDown().click();
                    break;
                }

                new WebDriverWait(driver, 10)
                        .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                        .until(ExpectedConditions.elementToBeClickable(loginPage.getLanguageMenu()));

            }
        }
    }

    // UiTest 4 and 5: Click on "setup parent support" -> Create a new account. Fill all the details to create an account.
    // Make this data driven so it could be executed multiple times.
    // Assert that "create account" button is disabled till all fields are filled.

    // Solution: 1. An excel file (setupparentsupport.xlsx) is used to store data for the field values. 2. Open Setup Parent Support
    // 3. Select create a new account. 4. The excel file is read and data is sent through dataProviders to drive the test
    // as many number of times equivalent to data present in excel

    @Test(dataProvider = "getExcelData")
    public void verifySetupParentSupportUsingExcel(String data) throws InterruptedException {

        String[] excelData = data.split(",");

        NavigationUtils navigationUtils = new NavigationUtils();

        navigationUtils.getToLogin(driver, prop);

        navigationUtils.getToParentSupportSetup(driver);

        OtherUtils otherUtils = new OtherUtils();

        ParentSupportAccountCreationPage parentSupportAccountCreationPage = new ParentSupportAccountCreationPage(driver);

        for (int i = 0; i <= excelData.length; i++) {

            Assert.assertFalse(otherUtils.isCreateAccountFieldEnabled(driver), "create account button enabled before all fields are filled");

            switch (i) {
                case 0:
                    parentSupportAccountCreationPage.getFirstName().sendKeys(excelData[i].toString());
                    break;
                case 1:
                    parentSupportAccountCreationPage.getLastName().sendKeys(excelData[i].toString());
                    break;
                case 2:
                    parentSupportAccountCreationPage.getEmail().sendKeys(excelData[i].toString());
                    break;
                case 3:
                    parentSupportAccountCreationPage.getUsername().sendKeys(excelData[i].toString());
                    break;
                case 4:
                    parentSupportAccountCreationPage.getPassword().sendKeys(excelData[i].toString());
                    break;
                case 5:
                    parentSupportAccountCreationPage.getcPassword().sendKeys(excelData[4].toString());
                    break;
            }
        }

        parentSupportAccountCreationPage.getCaptcha().sendKeys("c");
        Assert.assertTrue(otherUtils.isCreateAccountFieldEnabled(driver), "create account button disabled after all fields are filled");
    }

    // UiTest 4 and 5: Click on "setup parent support" -> Create a new account. Fill all the details to create an account.
    // Make this data driven so it could be executed multiple times.
    // Assert that "create account" button is disabled till all fields are filled.

    // Solution: 1. A JSON file (setupparentsupport.json) is used to store data for the field values. 2. Open Setup Parent Support
    // 3. Select create a new account. 4. The json file is read and data is sent through dataProviders to drive the test
    // as many number of times equivalent to data present in excel
    @Test(dataProvider = "getJsonData")
    public void verifySetupParentSupportUsingJson(String data) throws InterruptedException {

        String[] jsonData = data.split(",");

        NavigationUtils navigationUtils = new NavigationUtils();

        navigationUtils.getToLogin(driver, prop);

        navigationUtils.getToParentSupportSetup(driver);

        OtherUtils otherUtils = new OtherUtils();

        ParentSupportAccountCreationPage parentSupportAccountCreationPage = new ParentSupportAccountCreationPage(driver);

        for (int i = 0; i <= jsonData.length; i++) {

            Assert.assertFalse(otherUtils.isCreateAccountFieldEnabled(driver), "create account button enabled before all fields are filled");

            switch (i) {
                case 0:
                    parentSupportAccountCreationPage.getFirstName().sendKeys(jsonData[i].toString());
                    break;
                case 1:
                    parentSupportAccountCreationPage.getLastName().sendKeys(jsonData[i].toString());
                    break;
                case 2:
                    parentSupportAccountCreationPage.getEmail().sendKeys(jsonData[i].toString());
                    break;
                case 3:
                    parentSupportAccountCreationPage.getUsername().sendKeys(jsonData[i].toString());
                    break;
                case 4:
                    parentSupportAccountCreationPage.getPassword().sendKeys(jsonData[i].toString());
                    break;
                case 5:
                    parentSupportAccountCreationPage.getcPassword().sendKeys(jsonData[4].toString());
                    break;
            }
        }

        parentSupportAccountCreationPage.getCaptcha().sendKeys("c");
        Assert.assertTrue(otherUtils.isCreateAccountFieldEnabled(driver), "create account button disabled after all fields are filled");
    }

    @DataProvider
    public Object[] getExcelData() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("setupparentsupport.xlsx");

        //fileInputStream argument
        ArrayList<String> a = new ArrayList<String>();

        XSSFWorkbook workbook = new XSSFWorkbook(is);

        Object[] obj = null;

        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
        rows.next();
        obj = new Object[sheet.getPhysicalNumberOfRows() - 1];
        int numberOfRows = 0;
        while (rows.hasNext()) {

            Row r = rows.next();

            String columnValues = "";

            Iterator<Cell> cv = r.cellIterator();
            while (cv.hasNext()) {
                columnValues = columnValues + cv.next().getStringCellValue() + ",";
            }

            obj[numberOfRows] = columnValues;

            numberOfRows++;
        }

        return obj;
    }

    @DataProvider
    public Object[] getJsonData() throws IOException, ParseException {
        FileReader reader = new FileReader(getClass().getClassLoader().getResource("setupparentsupport.json").getFile());

        if (reader == null) {
            throw new FileNotFoundException("api data file apidata.json not found in the classpath");
        }

        JSONParser jsonParser = new JSONParser();

        Object obj = jsonParser.parse(reader);

        JSONArray jsonArray = (JSONArray) obj;

        Object[] data = new Object[jsonArray.size()];

        int i = 0;

        for (Object jsonObject : jsonArray) {
            data[i] = ((JSONObject) jsonObject).get("First Name").toString() + ","
                    + ((JSONObject) jsonObject).get("Last Name").toString() + ","
                    + ((JSONObject) jsonObject).get("Email Address").toString() + ","
                    + ((JSONObject) jsonObject).get("User Name").toString() + ","
                    + ((JSONObject) jsonObject).get("Password").toString();
            i++;
        }

        return data;
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }

}
