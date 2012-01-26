package framework;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;

import com.opera.core.systems.OperaDriver;

public class PageObjectHelper {
	protected WebDriver driver;
	private Random r = new Random();

	// -------------------
	/** write log info */
	private static final boolean IS_WRITE_LOG_INFO = false;

	/** Time to wait */
	protected int waitTime = 10;

	/** new line symbol */
	protected final String NEW_LINE = System.getProperty("line.separator");

	/** Path where all stored */
	protected String pathToFolder = "c:\\report\\";

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

	// ------------------------- Assets
	/**
	 * Compare equals if not equals - test fail
	 * 
	 * @param actual
	 *            Object - what to compare
	 * @param expected
	 *            Object - what we expect
	 * @param errorMessage
	 *            - message to add to log
	 */
	protected void assertEquals(Object actual, Object expected,
			String errorMessage) {
		// Our own assert
		if (!actual.equals(expected))
			errorHeppens(errorMessage + NEW_LINE
					+ " Wrong assertEquals   actual: " + actual
					+ " | expected: " + expected);
		Assert.assertEquals(actual, expected);
	}

	/**
	 * Compare equals if not equals - test fail
	 * 
	 * @param actual
	 *            Object - what to compare
	 * @param expected
	 *            Object - what we expect
	 */
	protected void assertEquals(Object actual, Object expected) {
		assertEquals(actual, expected, "");
	}

	/**
	 * Checking if condition is true
	 * 
	 * @param condition
	 *            - Boolean if false than test fails
	 * @param errorMessage
	 *            - message to add to log
	 */
	protected void assertTrue(Boolean condition, String errorMessage) {
		if (!condition)
			errorHeppens(errorMessage + NEW_LINE
					+ "Wrong assertTrue   actual: " + condition
					+ " | expected: true");
	}

	/**
	 * Test fail show error
	 * 
	 * @param errorMessage
	 */
	protected void assertShowError(String errorMessage) {
		errorHeppens(errorMessage + NEW_LINE);
	}

	/**
	 * Checking if condition is true
	 * 
	 * @param condition
	 *            - Boolean if false than test fails
	 */
	protected void assertTrue(Boolean condition) {
		assertTrue(condition, "");
	}

	// ------------------------- Getting driver info
	/**
	 * Is currently using driver is FileFox
	 * 
	 * @return
	 */
	protected boolean isDriverFireFox() {
		return driver instanceof FirefoxDriver;
	}

	/**
	 * Is currently using driver is Chrome
	 * 
	 * @return
	 */
	protected boolean isDriverChrome() {
		return driver instanceof ChromeDriver;
	}

	/**
	 * Is currently using driver is Opera
	 * 
	 * @return
	 */
	protected boolean isDriverOpera() {
		return driver instanceof OperaDriver;
	}

	/**
	 * Is currently using driver is InternetExplorer
	 * 
	 * @return
	 */
	protected boolean isDriverInternetExplorer() {
		return driver instanceof InternetExplorerDriver;
	}

	// ------------------------- Getting system properties
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

	// ------------------------- Errors
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

		// Screen shot
		boolean isScreenTaken = tackeScreenShot(prefix);

		// Printing to test NG report
		Reporter.log(errorMessage + NEW_LINE + "screenShot " + isScreenTaken
				+ " " + prefix);

		// Printing to console
		errorPrint(errorMessage + " " + prefix);

		// Create text file
		// createAndWriteTextFile(errorMessage + NEW_LINE +
		// driver.getCurrentUrl()
		// + NEW_LINE + driver.getTitle() + NEW_LINE, prefix);
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

	// ------------------------- Files
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

	// ------------------------- General methods
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
		if (getText(element).contains(text)) {
			return true;
		}
		return false;
	}

	// ------------------------- Click open
	/**
	 * Perform click on element
	 * If no element found than wait and click
	 * 
	 * @param by
	 *            - selector of element
	 */
	protected void click(WebElement element) {
		try {
			element.click();
		} catch (NoSuchElementException e) {
			waitForElement(element);
			try {
				element.click();
			} catch (Exception e2) {
				errorHeppens(e.getMessage());
			}
		}
	}

	/**
	 * Click on random element in list of elements
	 * @param listOfElenentsToClick - list 
	 */
	protected void click(List<WebElement> listOfElenentsToClick) {
		int size = listOfElenentsToClick.size();
		if (size <= 0) { //TODO starting from 0?
			for (int secondNow = 0; secondNow < waitTime; secondNow++) {
				size = listOfElenentsToClick.size();
				if (size > 0) break;
				wait(1);
			}
			if (size <= 0) assertShowError("No elements were found");
		}
		
		int index = r.nextInt(size);
		click(listOfElenentsToClick.get(index));
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

	// ------------------------- Forms
	/**
	 * Filling value to element
	 * If value > 100 chars than copy past value
	 * @param by
	 *            - what element
	 * @param value
	 *            - what to fill
	 */
	protected boolean fill(WebElement element, String value) {
		try {
			fillPrivat(element, value);
			return true;
		} catch (NoSuchElementException e) {
			waitForElement(element);
			try {
				fillPrivat(element, value);
				return true;
			} catch (Exception e2) {
				errorHeppens(e.getMessage());
			}
		}
		return false;
		
	}

	/**
	 * Private method for supporting fill
	 * Performs fill with out checks
	 * @param element
	 * @param value
	 */
	private void fillPrivat(WebElement element, String value) {
		//Clearing element
		try {
			element.clear();
		} catch (Exception e) {
			// TODO: show than element is not cleareable
		}
		
		
		if (value.length() < 100) {
			
			element.sendKeys(value);
		} else {
			//Fast fill
			
			//Saving to clipboard
			StringSelection ss = new StringSelection(value);
		    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
			
		    //Pasting value
			element.sendKeys(Keys.CONTROL + "v");
		}
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

	/**
	 * Selecting element in select by visible text
	 * @param element - web element of select
	 * @param visibleText - what text to select
	 */
	protected void selectByVisibleText(WebElement element, String visibleText) {
		new Select(element).selectByVisibleText(visibleText);
	}

	// -------------------------- Waiting
	/**
	 * Do nothing for some time
	 * 
	 * @param secondsToWait
	 *            - what time to wait in Seconds (sec)
	 */
	protected void wait(int secondsToWait) {
		long end = System.currentTimeMillis() + secondsToWait * 1000;
		while (System.currentTimeMillis() < end) {
		}
	}

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
	 * Waiting while text will disappear
	 * 
	 * @param text
	 * @return
	 */
	protected boolean waitForNOtextOnPage(final String text) {
		for (int secondNow = 0; secondNow < waitTime; secondNow++) {
			try {
				if (!isTextOnPage(text))
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
	 * Waits for element to be hidden The element should be present but become
	 * hidden
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

	// ------------------------- Getting element info
	/**
	 * Getting text of the element
	 * 
	 * @param by
	 *            - what element
	 * @return text of element
	 */
	protected String getText(WebElement element) {
		waitForElement(element);
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
	 * Element should be present and displayed on screen
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

	// ------------------------- Alerts
	/**
	 * Accepting java script alert
	 */
	protected void alertAccept() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (NoAlertPresentException e) {
			print("No alert present");
		}
		
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
	 * 
	 * @param string
	 */
	private void printlog(String string) {
		if (IS_WRITE_LOG_INFO) {
			println(string);
		}
	}
}
