package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Class for running actions in parallel in same time User perform() to start!
 */
public abstract class ParallelRun {
	private String driverStr;
	private boolean resulteGetted;

	/**
	 * Creating parallel browsers User perform() to start!
	 * 
	 * @param driverStr
	 *            - what driver to use see getWebDriver in {@link StaticHelper}
	 *            
	 */
	public ParallelRun(String driverStr) {
		this.driverStr = driverStr;
	}

	/**
	 * Getting web element in one by one browsers
	 * 
	 * @param driver
	 *            - getting element using this driver
	 * @return WebElement
	 */
	public abstract WebElement getWebElementForAction(WebDriver driver);

	/**
	 * Actions to perform on element in parallel
	 * 
	 * @param webElement
	 * @return return true if result that you are locking fore have been found
	 */
	public abstract boolean runInParallel(WebElement webElement);

	/**
	 * Maim method Preparing data and starts parallel actions
	 * @return true if at least one runInParallel have returned true.
	 */
	public boolean perform() {
		new StaticHelper();
		// Starting 2 browsers
		WebDriver driver1 = StaticHelper.getWebDriver(driverStr);
		WebDriver driver2 = StaticHelper.getWebDriver(driverStr);

		// getting web elements
		WebElement webElement1 = getWebElementForAction(driver1);
		WebElement webElement2 = getWebElementForAction(driver2);

		// Creating threads
		Thread thread1 = new Thread(new ActionsInSameTime(webElement1));
		Thread thread2 = new Thread(new ActionsInSameTime(webElement2));

		// Performing fast actions
		thread1.start();
		thread2.start();

		// Returning have we seen result or not
		return resulteGetted;
	}

	/**
	 * Class for separate threads
	 */
	public class ActionsInSameTime implements Runnable {

		private WebElement webElement;

		public ActionsInSameTime(WebElement webElement) {
			this.webElement = webElement;
		}

		public void run() {
			if (runInParallel(webElement)) {
				resulteGetted = true;
			}
		}

	}

}
