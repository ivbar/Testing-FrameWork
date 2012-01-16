package framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class PageObjectHelper {
	protected WebDriver driver;
	protected Random r = new Random();

	// -------------------
	/** write log info */
	private static final boolean IS_WRITE_LOG_INFO = false;
	
	/** Time to wait */
	protected int waitTime = 30;

	/** new line symbol */
	protected final String NEW_LINE = System.getProperty("line.separator");

	/** Path where all stored */
	protected String pathToFolder = "c:\\tmp\\";

	/** ThrowExeptions on fail and stop tests */
	protected boolean throwExeptions = true;

	/**
	 * Default constructor
	 * 
	 * @param webDriver
	 */
	public PageObjectHelper(WebDriver webDriver) {
		this.driver = webDriver;
	}

	// ------------------------- assets
	/**
	 * Compare equals if not equals - test fail
	 * 
	 * @param actual
	 *            Object - what to compare
	 * @param expected
	 *            Object - what we expect
	 */
	protected void assertEquals(Object actual, Object expected) {
		// Our own assert
		if (!actual.equals(expected))
			errorHeppens("Wrong assertEquals");
		Assert.assertEquals(actual, expected);
	}

	/**
	 * Checking if condition is true
	 * 
	 * @param condition
	 *            Boolean if false than test fails
	 */
	protected void assertTrue(Boolean condition) {
		if (!condition)
			errorHeppens("Wrong assertTrue");
		Assert.assertTrue(condition);
	}

	// ------------------------- getting system properties
	/**
	 * Getting current time
	 * 
	 * @param dateFormat
	 * @return
	 */
	protected String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	// ------------------------- errors
	/**
	 * What actions to perform what get error
	 */
	protected void errorHeppens(String errorMessage) {
		customErrorHandler(errorMessage);

		if (throwExeptions) {
			Assert.fail();
		}
	}

	/**
	 * Custom actions with error TODO: move it away from Framework
	 * 
	 * @param errorMessage
	 */
	protected void customErrorHandler(String errorMessage) {
		String prefix = now("yyyyMMddhhmmss");

		// Printing to console
		errorPrint(errorMessage + " " + prefix);

		//
		tackeScreenShot(prefix);
		createAndWriteTextFile(errorMessage + NEW_LINE + driver.getCurrentUrl()
				+ NEW_LINE + driver.getTitle() + NEW_LINE, prefix);
	}

	/**
	 * Printing error message
	 * 
	 * @param errorMes
	 *            - what to print
	 */
	protected void errorPrint(String errorMes) {
		lnprint(errorMes);
	}

	// ------------------------- files
	/**
	 * Tacking screen shot
	 * 
	 * @return true if a passed good
	 */
	protected boolean tackeScreenShot(String filePrefix) {
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		String fileName = pathToFolder + filePrefix + ".png";
		try {
			// TODO: add import
			FileUtils.copyFile(scrFile, new File(fileName));
		} catch (IOException e) {
			errorPrint("Screen shot fail");
			return false;
		}
		return true;
	}

	/**
	 * Create text file in default folder with text
	 * 
	 * @param text
	 *            - what to write to text file
	 * @param fileName
	 *            - name
	 * @return true if all fine
	 */
	protected boolean createAndWriteTextFile(String text, String fileName) {
		try {
			Writer output = null;
			File file = new File(pathToFolder + fileName + ".txt");
			output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			output.close();
		} catch (IOException e) {
			errorPrint("File write fail");
			return false;
		}
		return true;
	}

	// ------------------------- general methods
	/**
	 * Opens url
	 * 
	 * @param url
	 */
	protected void open(String url) {
		this.driver.get(url);
	}

	/**
	 * Search for text on page
	 * 
	 * @param text
	 *            - text to search
	 * @return true if we have such text
	 */
	protected boolean isTextOnPage(String text) {
		if (driver.getPageSource().contains(text))
			return true;
		return false;
	}

	/**
	 * Search for text on in some element
	 * 
	 * @param text
	 *            - text to search
	 * @param element
	 *            - in what element we are locking in
	 * @return true if we have such text
	 */
	protected boolean isTextInElement(String text, WebElement element) {
		if (element.getText().contains(text))
			return true;
		return false;
	}

	// ------------------------- click open
	/**
	 * Perform click on element
	 * 
	 * @param by
	 *            - selector of element
	 */
	protected void click(WebElement element) {
		try {
			element.click();
		} catch (NoSuchElementException e) {
			errorHeppens(e.getMessage());
		}
	}

	/**
	 * Perform click on element if it is present
	 * 
	 * @param by
	 *            - selector of element
	 */
	protected boolean clickIfPresent(WebElement element) {
		if (isElementPresent(element)) { // Is present
			click(element); // Than click
			return true;
		}
		return false;
	}

	/**
	 * Waiting for element to become visible and click on it
	 * 
	 * @param by
	 *            - selector of element
	 */
	protected void waitAndClick(WebElement element) {
		waitForVisible(element);
		click(element);
	}

	/**
	 * Check if there is such text in title
	 * 
	 * @param textInTitle
	 *            - string in title
	 * @return true if such text is it title
	 */
	protected boolean isTitleContains(String textInTitle) {
		if (driver.getTitle().contains(textInTitle))
			return true;
		return false;
	}

	// Forms
	/**
	 * Filling value to element
	 * 
	 * @param by
	 *            - what element
	 * @param value
	 *            - what to fill
	 */
	protected void fill(WebElement element, String value) {
		element.sendKeys(value);
	}

	/**
	 * Clearing element value and Filling value to element
	 * 
	 * @param by
	 *            - what element
	 * @param value
	 *            - what to fill
	 */
	protected void clearAndFill(WebElement element, String value) {
		element.clear();
		fill(element, value);
	}

	/**
	 * Submitting the form
	 * 
	 * @param by
	 *            - what element
	 */
	protected void submitForm(WebElement element) {
		element.submit();
	}

	// -------------------------- Waiting
	/**
	 * waits for a minute till page with required title appear
	 * 
	 * @param pageTitle
	 *            - what page to wait
	 * @return boolean - true when we get page, false if such page was not found
	 */
	protected boolean waitForPage(String pageTitle) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			try {
				if (driver.getTitle().contains(pageTitle))
					return true;
			} catch (Exception e) {
			}
			wait(1);
		}
		return false;
	}

	/**
	 * Waits for text to be present
	 * 
	 * @param by
	 *            - element locator Exception if after time put no element found
	 */
	protected boolean waitForTextOnPage(final String text) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			try {
				if (isTextOnPage(text))
					return true;
			} catch (Exception e) {
			}
			wait(1);
		}
		return false;
	}

	/**
	 * Waiting for element present for some time
	 * 
	 * @param by
	 *            - what element
	 * @param timeToWait
	 *            - Seconds to wait
	 * @return true if element become present in time
	 */
	protected boolean waitForElement(WebElement element) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			if (isElementPresent(element))
				return true;
			wait(1);
		}
		return false;
	}
	
	/**
	 * Waits for element to be shown
	 * 
	 * @param by
	 *            - element
	 * @return true - if element was finally found, false - if no element found
	 */
	protected boolean waitForVisible(WebElement element) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			if (element.isDisplayed()) {
				return true;
			}
			wait(1);
		}
		return false;
	}

	/**
	 * Waits for element to be hidden
	 * 
	 * @param by
	 *            - element
	 * @return true - if element was finally found, false - if no element found
	 */
	protected boolean waitForNotVisible(WebElement element) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			if (!element.isDisplayed())
				return true;
			wait(1);
		}
		return false;
	}

	/**
	 * Do no thing for some time
	 * 
	 * @param timeToWait
	 *            - what time to wait in Seconds (sec)
	 */
	protected void wait(int timeToWait) {
		long end = System.currentTimeMillis() + timeToWait * 1000;
		while (System.currentTimeMillis() < end) {
		}
	}

	// --------------- Getting element info
	/**
	 * Getting text of the element
	 * 
	 * @param by
	 *            - what element
	 * @return text of element
	 */
	protected String getText(WebElement element) {
		String text = "";
		try {
			text = element.getText();
			if (text.isEmpty())
				text = element.getAttribute("value"); // For input fields
		} catch (NoSuchElementException e) {
			errorHeppens(e.getMessage());
		}
		return text;
	}

	/**
	 * If we can locate element on screen
	 * 
	 * @param by
	 *            - what element
	 * @return true - there is such element, false - no element
	 */
	protected boolean isElementPresent(WebElement element) {
		try {
			if (element != null && element.isDisplayed()) {
				printlog("yes element");
				return true;
			} else {
				printlog("no element");
				return false;
			}
		} catch (NoSuchElementException e) {
			printlog("no element");
			return false;
		}
	}

	// Alerts
	/**
	 * Accepting java script alert
	 */
	protected void alertAccept() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	// ------------------------ Printing
	/**
	 * Show info in console
	 * 
	 * @param str
	 */
	protected void print(String str) {
		System.out.print(str);
	}

	/**
	 * Printing data to command line + new line
	 * 
	 * @param str
	 *            String
	 */
	protected void println(String str) {
		System.out.println(str);
	}

	/**
	 * new line + Printing data to vommand line
	 * 
	 * @param str
	 *            String
	 */
	protected void lnprint(String str) {
		println("");
		print(str);
	}

	/**
	 * Writing to log if IS_WRITE_LOG_INFO 
	 * @param string
	 */
	private void printlog(String string) {
		if (IS_WRITE_LOG_INFO) {
			println(string);
		}
	}
}
