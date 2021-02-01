package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class HomePage {
    private final WebDriverWait webDriverWait;
    private final WebDriver driver;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);

        this.driver = webDriver;
        this.webDriverWait = new WebDriverWait(webDriver, 10);
    }

    @FindBy(id = "logoutForm")
    private WebElement logoutForm;

    @FindBy(css = "#nav-notes-tab")
    private WebElement notesTab;

    @FindBy(css = "#create-note-btn")
    private WebElement createNoteBtn;

    @FindBy(css = "#note-title")
    private WebElement inputNoteTitle;

    @FindBy(css = "#note-description")
    private WebElement inputNoteDescription;

    @FindBy(css = "#note-form")
    private WebElement formNote;

    @FindBy(css = ".th-note-title")
    private WebElement tableNoteTitle;

    @FindBy(css = "#nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(css = "#create-credentials-btn")
    private WebElement createCredentialsBtn;

    @FindBy(css = "#credential-url")
    private WebElement inputCredentialsUrl;

    @FindBy(css = "#credential-username")
    private WebElement inputCredentialsUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredentialsPassword;

    @FindBy(css = "#formCredentials")
    private WebElement formCredentials;

    @FindBy(css = ".th-credentials-url")
    private WebElement tableCredentialsUrl;

    @FindBy(css = "edit-cred")
    private WebElement credentialsEditBtn;

    public String getPageUrl() {
        return "/home";
    }

    public String getPageTitle() {
        return "Home";
    }

    public void logUserOut() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", logoutForm);
    }

    public void createCredentials(String username, String url, String plainPassword) {
        this.waitToBeClickable(this.createCredentialsBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createCredentialsBtn);

        this.waitToBeVisible(this.inputCredentialsUrl);
        this.waitToBeVisible(this.inputCredentialsUsername);
        this.waitToBeVisible(this.inputCredentialsPassword);

        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url + "';", inputCredentialsUrl);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", inputCredentialsUsername);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + plainPassword + "';", inputCredentialsPassword);
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", formCredentials);
    }

    public void updateCredentials(String url, String username, String password) {
        if (!this.formCredentials.isDisplayed()) {
            this.waitToBeVisible(this.formCredentials);
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url + "';", inputCredentialsUrl);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", inputCredentialsUsername);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", inputCredentialsPassword);
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", formCredentials);
    }

    public void createNote(String title, String description) {
        this.waitToBeClickable(this.createNoteBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createNoteBtn);

        this.waitToBeVisible(this.inputNoteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", inputNoteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", inputNoteDescription);
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", formNote);
    }

    public void updateNote(WebElement editButtonOfNote, String newTitle, String newDescription) {
        this.waitToBeClickable(editButtonOfNote);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButtonOfNote);

        this.waitToBeVisible(this.inputNoteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + newTitle + "';", inputNoteTitle);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + newDescription + "';", inputNoteDescription);
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", formNote);
    }

    public void switchToNotesTab() {
        this.waitToBeClickable(this.notesTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);
    }

    public void switchToCredentialsTab() {
        this.waitToBeClickable(this.credentialsTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);
    }

    public void waitForNotesList() {
        this.waitToBeVisible(this.tableNoteTitle);
    }

    public void waitForCredentialsList() {
        this.waitToBeVisible(this.tableCredentialsUrl);
    }

    public WebElement waitToBeClickable(WebElement element) {
        return this.webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitToBeVisible(WebElement... element) {
        this.webDriverWait.until(ExpectedConditions.visibilityOfAllElements(element));
    }
}
