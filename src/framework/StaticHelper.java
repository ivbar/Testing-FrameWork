package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Helper without its own fields
 */
public class StaticHelper {

	/**
	 * Getting web driver on driver string 
	 * 0 - No driver 
	 * 1 - FirefoxDriver 
	 * 2 - InternetExplorerDriver 
	 * 3 - ChromeDriver
	 * 
	 * 10 - HtmlUnitDriver with JS (No ui) 
	 * 10 - HtmlUnitDriver with No JS (No ui)
	 * 
	 * default: 1 - FirefoxDriver
	 * 
	 * @param driverStr
	 * @return WebDriver
	 */
	public static WebDriver getWebDriver(String driverStr) {
		try {
			switch (Integer.parseInt(driverStr)) {
			case 0:
				return null;
			case 1:
				return new FirefoxDriver();
			case 2:
				return new InternetExplorerDriver();
			case 3:
				return new ChromeDriver();
			case 10:
				HtmlUnitDriver driver = new HtmlUnitDriver();
				((HtmlUnitDriver) driver).setJavascriptEnabled(true);
				return driver;
			case 11:
				return new HtmlUnitDriver();
			default:
				return new FirefoxDriver();
			}
		} catch (NumberFormatException e) {
			return new FirefoxDriver();
		}
	}

}
