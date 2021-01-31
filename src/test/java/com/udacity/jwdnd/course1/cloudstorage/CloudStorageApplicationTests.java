package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;
    private static WebDriver driver;
    private String baseURL;

    private LoginPage loginPage;
    private SignupPage signupPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.baseURL = "http://localhost:" + this.port;
        driver = new ChromeDriver();

        this.loginPage = new LoginPage(driver);
        this.signupPage = new SignupPage(driver);
        this.homePage = new HomePage(driver);
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
        driver = null;
    }

    @Test
    public void getLoginPage() {
        driver.get(this.baseURL + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testAccessOnlyLoginAndSignupIfNotLoggedIn() {
        driver.get(this.baseURL + loginPage.getPageUrl());
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());

        driver.get(this.baseURL + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get(this.baseURL + "/home");
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());

        driver.get(this.baseURL + "/notes");
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());

        driver.get(this.baseURL + "/credentials");
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());

        driver.get(this.baseURL + "/files");
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());
    }

    @Test
    public void testCreateNewUserLoginVerifyHomeIsAccessibleAndLogout() {
        String username = RandomString.make(10);
        String password = "458BkHH6AskW54Aa@#";

        driver.get(this.baseURL + signupPage.getPageUrl());
        signupPage.registerNewUser(RandomString.make(5), RandomString.make(5), username, password);
        Assertions.assertTrue(signupPage.isUserSignUpSuccessfull());

        driver.get(this.baseURL + loginPage.getPageUrl());
        loginPage.logUserIn(username, password);
        Assertions.assertEquals(homePage.getPageTitle(), driver.getTitle());

        homePage.logUserOut();
        Assertions.assertEquals(loginPage.getPageTitle(), driver.getTitle());
        Assertions.assertTrue(loginPage.isUserLoggedOut());
    }

    @Test
    public void testCreateNoteVerifyIsDisplayed() {
        this.createAndLogUserIn();

        Note note = this.createNote();

        homePage.switchToNotesTab();
        homePage.waitForNotesList();

        boolean noteExists = driver.findElements(By.className("th-note-title"))
                .stream()
                .anyMatch(webElement -> webElement.getText().equals(note.getNoteTitle()));

        Assertions.assertTrue(noteExists);
    }

    @Test
    public void testEditAnExistingNoteAndVerifyChanges() {
        this.createAndLogUserIn();

        Note note = this.createNote();

        homePage.switchToNotesTab();
        homePage.waitForNotesList();

        String path = String.format("//*[@data-note-title='%s']", note.getNoteTitle());
        homePage.updateNote(driver.findElement(By.xpath(path)), "updatedTitle", "updatedDesc");

        homePage.switchToNotesTab();
        homePage.waitForNotesList();

        boolean updatedNoteExists = driver.findElements(By.className("th-note-title"))
                .stream()
                .anyMatch(webElement -> webElement.getText().equals("updatedTitle"));

        Assertions.assertTrue(updatedNoteExists);
    }

    @Test
    public void testDeleteNoteAndVerifyIsNoLongerDisplayed() {
        this.createAndLogUserIn();

        this.createNote();
        Note note = this.createNote();

        homePage.switchToNotesTab();
        homePage.waitForNotesList();

        String path = String.format("//*[@data-delete-note-title='%s']", note.getNoteTitle());
        driver.findElement(By.xpath(path)).submit();

        homePage.switchToNotesTab();
        homePage.waitForNotesList();

        boolean noteNotDisplayed = driver.findElements(By.className("th-note-title"))
                .stream()
                .noneMatch(webElement -> webElement.getText().equals(note.getNoteTitle()));

        Assertions.assertTrue(noteNotDisplayed);
    }

    @Test
    public void testCreateCredentialsVerifyTheyAreDisplayedVerifyDidsplayedPasswordIsEncrypted() {
        this.createAndLogUserIn();

        Credential credential = this.createCredentials();

        homePage.switchToCredentialsTab();
        homePage.waitForCredentialsList();

        boolean credentialExists = driver.findElements(By.className("th-credentials-url"))
                .stream()
                .anyMatch(el -> el.getText().equals(credential.getUrl()));

        Assertions.assertTrue(credentialExists);

        boolean passwordIsNotDisplayed = driver.findElements(By.className("td-credentials-password"))
                .stream()
                .noneMatch(el -> el.getText().equals(credential.getPassword()));

        Assertions.assertTrue(passwordIsNotDisplayed);
    }

    @Test
    public void testCreateCredentialsCheckEditablePasswordIsUnecryptedEditVerifyChanges() {
        this.createAndLogUserIn();

        Credential credential = this.createCredentials();

        homePage.switchToCredentialsTab();
        homePage.waitForCredentialsList();

        String path = String.format("//*[@data-cred-url='%s']", credential.getUrl());
        new WebDriverWait(driver, 10).until(w -> driver.findElement(By.xpath(path))).click();

        String passwordInputValue = driver.findElement(By.name("password")).getAttribute("value");
        boolean viewablePasswordEqualsSubmitted = credential.getPassword().equals(passwordInputValue);

        Assertions.assertTrue(viewablePasswordEqualsSubmitted);

        homePage.updateCredentials("updatedUrl", "updatedUsername", "passUp");

        homePage.switchToCredentialsTab();
        homePage.waitForCredentialsList();

        boolean updatedUrlDisplayed = driver.findElements(By.className("th-credentials-url"))
                .stream()
                .anyMatch(el -> el.getText().equals("updatedUrl"));

        boolean updatedUsernameDisplayed = driver.findElements(By.className("td-credentials-username"))
                .stream()
                .anyMatch(el -> el.getText().equals("updatedUsername"));

        Assertions.assertTrue(updatedUrlDisplayed);
        Assertions.assertTrue(updatedUsernameDisplayed);
    }

    @Test
    public void testDeleteCredentialVerifyNotDisplayed() {
        this.createAndLogUserIn();

        Credential credential = this.createCredentials();

        homePage.switchToCredentialsTab();
        homePage.waitForCredentialsList();

        String path = String.format("//*[@data-delete-url='%s']", credential.getUrl());
        driver.findElement(By.xpath(path)).submit();

        homePage.switchToCredentialsTab();

        boolean credentialNotDisplayed = driver.findElements(By.className("th-credentials-url"))
                .stream()
                .noneMatch(el -> el.getText().equals(credential.getUrl()));

        Assertions.assertTrue(credentialNotDisplayed);
    }

    private Note createNote() {
        String noteTitle = RandomString.make(15);
        String noteDescription = RandomString.make(20);

        driver.get(this.baseURL + homePage.getPageUrl());

        homePage.switchToNotesTab();
        homePage.createNote(noteTitle, noteDescription);

        Note note = new Note();
        note.setNoteTitle(noteTitle);
        note.setNoteDescription(noteDescription);

        return note;
    }

    private Credential createCredentials() {
        String url = "https://www.randomurl.de";
        String username = RandomString.make(10);
        String password = "pass";

        driver.get(this.baseURL + homePage.getPageUrl());

        homePage.switchToCredentialsTab();
        homePage.createCredentials(username, url, password);

        Credential credential = new Credential();
        credential.setUrl(url);
        credential.setUsername(username);
        credential.setPassword(password);

        return credential;
    }

    private void createAndLogUserIn() {
        String username = RandomString.make(10);
        String password = "98AasERkj6AskW54Aa@#";

        driver.get(this.baseURL + signupPage.getPageUrl());
        signupPage.registerNewUser(RandomString.make(5), RandomString.make(5), username, password);

        driver.get(this.baseURL + loginPage.getPageUrl());
        loginPage.logUserIn(username, password);
    }
}
