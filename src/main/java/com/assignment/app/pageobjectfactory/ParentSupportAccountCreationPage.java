package com.assignment.app.pageobjectfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ParentSupportAccountCreationPage {

    WebDriver driver;

    public ParentSupportAccountCreationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, 'Firstname')]")
    WebElement firstName;

    @FindBy(xpath = "//input[contains(@id, 'Lastname')]")
    WebElement lastName;

    @FindBy(xpath = "//input[contains(@id, 'Emailaddress')]")
    WebElement email;

    @FindBy(xpath = "//input[contains(@id, 'Createparentusername')]")
    WebElement username;

    @FindBy(xpath = "//input[contains(@id, 'Createparentpassword')]")
    WebElement password;

    @FindBy(xpath = "//input[contains(@id, 'Confirmpassword')]")
    WebElement cPassword;

    @FindBy(xpath = "//span[text() = 'CREATE ACCOUNT']")
    WebElement createAccount;

    @FindBy(xpath = "//input[contains(@id, 'ValidateCaptcha')]")
    WebElement captcha;

    public WebElement getFirstName() {
        return firstName;
    }

    public WebElement getLastName() {
        return lastName;
    }

    public WebElement getEmail() {
        return email;
    }

    public WebElement getUsername() {
        return username;
    }

    public WebElement getCreateAccount() {
        return createAccount;
    }

    public WebElement getPassword() {
        return password;
    }

    public WebElement getcPassword() {
        return cPassword;
    }

    public WebElement getCaptcha() {
        return captcha;
    }

}
