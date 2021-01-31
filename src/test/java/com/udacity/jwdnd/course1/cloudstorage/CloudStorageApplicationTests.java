package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;
    private WebDriver driver;
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
        this.driver = new ChromeDriver();

        this.loginPage = new LoginPage(this.driver);
        this.signupPage = new SignupPage(this.driver);
        this.homePage = new HomePage(this.driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
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

        boolean updateNoteExists = driver.findElements(By.className("th-note-title"))
                .stream()
                .anyMatch(webElement -> webElement.getText().equals("updatedTitle"));

        Assertions.assertTrue(updateNoteExists);
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

        boolean noteExists = driver.findElements(By.className("th-note-title"))
                .stream()
                .noneMatch(webElement -> webElement.getText().equals(note.getNoteTitle()));

        Assertions.assertTrue(noteExists);
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

    private void createAndLogUserIn() {
        String username = RandomString.make(10);
        String password = "98AasERkj6AskW54Aa@#";

        driver.get(this.baseURL + signupPage.getPageUrl());
        signupPage.registerNewUser(RandomString.make(5), RandomString.make(5), username, password);

        driver.get(this.baseURL + loginPage.getPageUrl());
        loginPage.logUserIn(username, password);
    }
}
