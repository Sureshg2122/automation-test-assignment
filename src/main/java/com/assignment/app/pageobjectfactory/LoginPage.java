package com.assignment.app.pageobjectfactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "txtLoginEmail")
    WebElement username;

    @FindBy(className = "accountDetailsLangDropDown")
    WebElement languageDropDown;

    @FindBy(css="div[role='menu']")
    WebElement languageMenu;

    @FindBy(xpath = "//div[@role='menu']/div/span[@role='menuitem']")
    List<WebElement> languageOptions;

    @FindBy(tagName = "iframe")
    List<WebElement> iframes;

    @FindBy(id = "spanDone")
    WebElement closeiFrame;

    @FindBy(xpath = "//button[@id='SI_0005']/div/div")
    WebElement continueButton;

    @FindBy(className = "childSupportLink")
    WebElement setUpParentSupport;

    @FindBy(xpath = "//*[text()='CREATE A NEW ACCOUNT']")
    WebElement createANewAccount;

    public WebElement getSetUpParentSupport() {
        return setUpParentSupport;
    }

    public WebElement getCreateANewAccount() {
        return createANewAccount;
    }

    public WebElement getUsername() {
        return username;
    }

    public WebElement getLanguageMenu() {
        return languageMenu;
    }

    public List<WebElement> getLanguageOptions() {
        return languageOptions;
    }

    public String getLoginFrameId() {
        return "contentIframe";
    }

    public List<WebElement> getIframes() {
        return iframes;
    }

    public WebElement getCloseiFrame() {
        return closeiFrame;
    }

    public WebElement getLanguageDropDown() {
        return languageDropDown;
    }

    public WebElement languageOptionSelector(int index) {
        return driver.findElement(By.xpath("(//div[@role='menu']/div/span[@role='menuitem']/div/div/div)["+index+"]"));
    }

    public WebElement getContinueButton() {
        return continueButton;
    }

}
