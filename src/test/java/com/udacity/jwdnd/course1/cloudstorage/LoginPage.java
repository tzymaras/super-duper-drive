package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "messageLoggedOut")
    private WebElement messageLoggedOut;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public String getPageUrl() {
        return "/login";
    }

    public String getPageTitle() {
        return "Login";
    }

    public void logUserIn(String username, String password) {
        this.inputUsername.sendKeys(username);
        this.inputPassword.sendKeys(password);
        this.submitButton.click();
    }

    public boolean isUserLoggedOut() {
        return this.messageLoggedOut.isDisplayed();
    }
}
