package lambdatest.selenium101;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestBase {
	protected RemoteWebDriver driver;
	protected String Status = "failed";

	// LambdaTest credentials (replace with valid credentials)
	private String username = "ramya_kommanaboyana";
	private String accessKey = "LT_4HwbYq3WzvISdeO4DufzluJyRFrNnSekGiXtbrQjNK9UGxI";
	private String gridURL = "https://hub.lambdatest.com/wd/hub";

	@Parameters({ "browserName", "browserVersion", "osVersion", "url" })
	@BeforeMethod
	public void setup(String browserName, String browserVersion, String osVersion, String url, Method method,
			ITestContext ctx) throws MalformedURLException {

		// LambdaTest options configuration
		HashMap<String, Object> ltOptions = new HashMap<>();
		ltOptions.put("username", username);
		ltOptions.put("accessKey", accessKey);
		ltOptions.put("visual", true);
		ltOptions.put("video", true);
		ltOptions.put("network", true);
		ltOptions.put("project", "Untitled");
		ltOptions.put("build", "Selenium101-Build");
		ltOptions.put("name", method.getName()); // Test method name
		ltOptions.put("platform", osVersion);
		ltOptions.put("browserName", browserName);
		ltOptions.put("browserVersion", browserVersion);
		ltOptions.put("w3c", true);
		ltOptions.put("plugin", "Java-TestNG");

		// Setting desired capabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("LT:Options", ltOptions);

		// Browser setup based on browserName
		if (browserName.equalsIgnoreCase("Chrome")) {
			ChromeOptions browserOptions = new ChromeOptions();
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", osVersion);
			browserOptions.setCapability("LT:Options", ltOptions);
			driver = new RemoteWebDriver(new URL(gridURL), browserOptions);
		} else if (browserName.equalsIgnoreCase("Firefox")) {
			FirefoxOptions browserOptions = new FirefoxOptions();
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", osVersion);
			browserOptions.setCapability("LT:Options", ltOptions);
			driver = new RemoteWebDriver(new URL(gridURL), browserOptions);
		} else if (browserName.equalsIgnoreCase("Edge")) {
			EdgeOptions browserOptions = new EdgeOptions();
			browserOptions.setCapability("browserVersion", browserVersion);
			browserOptions.setCapability("platformName", osVersion);
			browserOptions.setCapability("LT:Options", ltOptions);
			driver = new RemoteWebDriver(new URL(gridURL), browserOptions);
		} else {
			throw new IllegalArgumentException("Browser not supported: " + browserName);
		}

		// Browser setup
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(28));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		// Open the test URL
		driver.get(url);
	}

	public RemoteWebDriver getDriver() {
		return driver;
	}

	@Test
	public void testScenario1() throws InterruptedException {
		// Step 1: Open LambdaTest's Selenium Playground
		driver.get("https://www.lambdatest.com/selenium-playground");

		// Step 2: Click "Simple Form Demo"
		WebElement singleInputFieldLink = driver.findElement(By.linkText("Simple Form Demo"));
		singleInputFieldLink.click();

		// Step 3: Validate that the URL contains 'simple-form-demo'
		String currentURL = driver.getCurrentUrl();
		Assert.assertTrue(currentURL.contains("simple-form-demo"), "URL does not contain 'simple-form-demo'");

		// Step 4: Create a variable for a string value
		String message = "Welcome to LambdaTest";

		// Step 5: Enter the value in the message box
		WebElement messageInput = driver.findElement(By.id("user-message"));
		messageInput.sendKeys(message);

		// Step 6: Click "Show Message"
		WebElement showMessageButton = driver.findElement(By.id("showInput"));
		showMessageButton.click();

		// Step 7: Validate whether the same text message is displayed in the right-hand
		// panel
		WebElement displayedMessage = driver.findElement(By.xpath("//p[@id='message']"));
		String displayedText = displayedMessage.getText();
		Assert.assertEquals(displayedText, message, "Displayed message does not match input message");

		// Set Status to passed if all assertions are successful
		Status = "passed";
	}

	@Test
	public void testScenario2() {
		// Step 1: Open LambdaTest's Selenium Playground
		driver.get("https://www.lambdatest.com/selenium-playground");

		// Step 2: Click "Drag & Drop Sliders"
		driver.findElement(By.linkText("Drag & Drop Sliders")).click();

		// Step 3: Select the slider with default value 15 and drag the bar to make it
		// 95
		WebElement slider = driver.findElement(By.xpath("//input[@value='15']"));
		WebElement rangeValue = driver.findElement(By.id("rangeSuccess"));

		// Create an Actions object to perform drag-and-drop
		Actions action = new Actions(driver);

		// Perform the action to move the slider
		action.clickAndHold(slider).moveByOffset(215, 0).release().perform();

		// Adjust offset if needed (you may need to tweak this depending on your screen
		// resolution)

		// Step 4: Validate whether the range value shows 95
		Assert.assertEquals(rangeValue.getText(), "95", "Test failed: The range value is incorrect.");

		// Output success message if the test passes
		System.out.println("Test passed: The range value is 95.");

		// Set the test status to passed
		Status = "passed";
	}

	@Test
	public void testScenario3() {
		// Step 1: Open LambdaTest's Selenium Playground
		driver.get("https://www.lambdatest.com/selenium-playground");

		// Step 2: Click "Input Form Submit"
		driver.findElement(By.linkText("Input Form Submit")).click();

		// Step 3: Click "Submit" without filling in any information
		driver.findElement(By.xpath("//button[text()='Submit']")).click();

		// Step 4: Capture and validate the inline validation message
		WebElement nameField = driver.findElement(By.name("name"));
		String validationMessage = nameField.getAttribute("validationMessage");

		// Assert the validation message
		Assert.assertEquals(validationMessage, "Please fill out this field.", "Validation message mismatch");

		// Step 5: Fill in Name, Email, and other fields
		driver.findElement(By.name("name")).sendKeys("Ramya");
		driver.findElement(By.id("inputEmail4")).sendKeys("ramya@example.com");
		driver.findElement(By.id("inputPassword4")).sendKeys("Ramyasree@12345");
		driver.findElement(By.id("company")).sendKeys("persistent");
		driver.findElement(By.id("websitename")).sendKeys("www.persistentsystems.com");
		driver.findElement(By.name("city")).sendKeys("united States");
		driver.findElement(By.name("address_line1")).sendKeys("Strret 1, House 123");
		driver.findElement(By.name("address_line2")).sendKeys("GL Street");
		driver.findElement(By.id("inputState")).sendKeys("Cali");
		driver.findElement(By.id("inputZip")).sendKeys("123456");

		// Step 6: Select "United States" from the country drop-down
		Select countryDropdown = new Select(driver.findElement(By.name("country")));
		countryDropdown.selectByVisibleText("United States");

		// Step 7: Click Submit
		driver.findElement(By.xpath("//button[text()='Submit']")).click();

		// Step 8: Validate the success message
		WebElement successMessage = driver.findElement(By.className("success-msg"));
		Assert.assertEquals(successMessage.getText(), "Thanks for contacting us, we will get back to you shortly.",
				"Success message mismatch");

		// Mark test as passed
		Status = "passed";

		// Output a message indicating the test passed
		System.out.println("Test Passed: Form submitted successfully.");
	}

	@AfterMethod
	public void tearDown() {
		// Log the status to LambdaTest (or any other test reporting tool you're using)
		driver.executeScript("lambda-status=" + Status);

		// Quit the driver after test completion
		if (driver != null) {
			driver.quit();
		}
	}
}
