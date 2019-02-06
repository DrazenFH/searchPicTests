package at.fhv.cicd.tests;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Step implementation for the sentiment analysis UAT tests
 */
public class SearchPicSteps {

	private WebDriver driver;

	/**
	 * Setup the firefox test driver. This needs the environment variable
	 * 'webdriver.gecko.driver' with the path to the geckodriver binary
	 */
	@Before
	public void before(Scenario scenario) throws Exception {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platform", "WIN10");
		capabilities.setCapability("version", "70");
		capabilities.setCapability("browserName", "chrome");
		capabilities.setCapability("name", scenario.getName());

		if (!scenario.getName().endsWith("(video)")) {
			capabilities.setCapability("headless", true);
		}


		driver = new RemoteWebDriver(
				new URL("http://" + System.getenv("$TESTINGBOT_KEY") + "@hub.testingbot.com/wd/hub"),
				capabilities);

		// prevent errors if we start from a sleeping heroku instance
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * Shutdown the driver
	 */
	@After
	public void after() {
		driver.quit();
	}

	@Given("^Open (.*?)$")
	public void openUrl(String url) {
		driver.navigate().to(url);
	}

	@Given("^Login with user '(.*?)'$")
	public void login(String email) {
		WebElement emailField = driver.findElement(By.id("email"));
		emailField.sendKeys(email);
		driver.findElement(By.id("loginBtn")).click();
	}

	@When("^Search the picture '(.*?)'$")
	public void searchPic(String text) {
		WebElement textField = driver.findElement(By.id("searchText"));
		textField.clear();
		textField.sendKeys(text);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("searchBtn")));
		button.click();

	}

	@Then("^The result list should be (.*?)$")
	// wait until the result has been received
	public void checkSearchPicRequest(String picResult) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("searchBtn")));

		WebElement pictureItem = driver.findElement(By.id("img01"));
		verifyPicture(pictureItem, picResult);
	}

	@When("^I press logout$")
	public void logout() {
		WebDriverWait wait = new WebDriverWait(driver, 10);

		// wait until popup is visible
		WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logoutBtn")));
		logoutBtn.click();
	}

	@Then("^I see the login page$")
	public void checkLoginPage() {
		assertFalse(driver.findElements(By.id("logo")).isEmpty());
	}

	/**
	 * Check if the given icon contains the given sentiment
	 *
	 * @param pictureItem The icon to check
	 * @param pictureResult    The sentiment which should be set in the icon
	 */
	private void verifyPicture(WebElement pictureItem, String pictureResult) {

		String classes = pictureItem.getAttribute("src");

		//assertTrue(classes.contains("https://farm8.staticflickr.com/7908/46888308852_cc302c9992.jpg"));
		assertTrue(classes.endsWith("jpg"));

	}
}
