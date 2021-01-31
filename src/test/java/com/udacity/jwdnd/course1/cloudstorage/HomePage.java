package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.*;

public class HomePage {
    private final WebDriverWait webDriverWait;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
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
        this.logoutForm.submit();
    }

    public void createCredentials(String username, String url, String plainPassword) {
        this.waitToBeClickable(this.createCredentialsBtn).click();
        this.waitToBeVisible(this.inputCredentialsUrl);
        this.waitToBeVisible(this.inputCredentialsUsername);
        this.waitToBeVisible(this.inputCredentialsPassword);

        this.inputCredentialsUrl.sendKeys(url);
        this.inputCredentialsUsername.sendKeys(username);
        this.inputCredentialsPassword.sendKeys(plainPassword);

        this.formCredentials.submit();
    }

    public void updateCredentials(String url, String username, String password) {
        if (!this.formCredentials.isDisplayed()) {
            this.waitToBeVisible(this.formCredentials);
        }

        this.inputCredentialsUrl.clear();
        this.inputCredentialsUsername.clear();
        this.inputCredentialsPassword.clear();

        this.inputCredentialsUrl.sendKeys(url);
        this.inputCredentialsUsername.sendKeys(username);
        this.inputCredentialsPassword.sendKeys(password);

        this.formCredentials.submit();
    }

    public void createNote(String title, String description) {
        this.waitToBeClickable(this.createNoteBtn).click();
        this.waitToBeVisible(this.inputNoteTitle);

        this.inputNoteTitle.sendKeys(title);
        this.inputNoteDescription.sendKeys(description);

        this.formNote.submit();
    }

    public void updateNote(WebElement editButtonOfNote, String newTitle, String newDescription) {
        this.waitToBeClickable(editButtonOfNote).click();
        this.waitToBeVisible(this.inputNoteTitle);

        this.inputNoteTitle.clear();
        this.inputNoteDescription.clear();

        this.inputNoteTitle.sendKeys(newTitle);
        this.inputNoteDescription.sendKeys(newDescription);

        this.formNote.submit();
    }

    public void switchToNotesTab() {
        this.waitToBeClickable(this.notesTab).click();
    }

    public void switchToCredentialsTab() {
        this.waitToBeClickable(this.credentialsTab).click();
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
