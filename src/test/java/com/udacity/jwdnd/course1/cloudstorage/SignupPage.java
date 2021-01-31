package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

public class SignupPage {
    @FindBy(id = "inputFirstName")
    private WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    private WebElement inputLastName;

    @FindBy(id = "inputUsername")
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "messageSignupSuccess")
    private WebElement registerSuccessMsg;

    public SignupPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public String getPageUrl() {
        return "/signup";
    }

    public void fillInForm(String firstName, String lastName, String username, String password) {
        this.inputUsername.sendKeys(username);
        this.inputFirstName.sendKeys(firstName);
        this.inputLastName.sendKeys(lastName);
        this.inputPassword.sendKeys(password);
    }

    public void submitForm() {
        this.submitButton.click();
    }

    public void registerNewUser(String firstName, String lastName, String username, String password) {
        this.fillInForm(firstName, lastName, username, password);
        this.submitForm();
    }

    public boolean isUserSignUpSuccessfull() {
        return this.registerSuccessMsg.isDisplayed();
    }
}
