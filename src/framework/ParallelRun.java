package framework;

import org.openqa.selenium.WebDriver;

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
	 *            - what driver to use see getWebDriver in {@link StaticHelper#getWebDriver(String)}
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
	public abstract PageObjectHelper getPageObjectbjectForAction(WebDriver driver);

	/**
	 * Actions to perform on element in parallel
	 * 
	 * @param webElement
	 * @param driver 
	 * @return return true if result that you are locking fore have been found
	 */
	public abstract boolean runInParallel(PageObjectHelper pageObject);

	/**
	 * Maim method Preparing data and starts parallel actions
	 * @return true if at least one runInParallel have returned true.
	 */
	public boolean perform() {
		// Starting 2 browsers
		WebDriver driver1 = StaticHelper.getWebDriver(driverStr);
		WebDriver driver2 = StaticHelper.getWebDriver(driverStr);

		// getting web elements
		PageObjectHelper pageObject1 = getPageObjectbjectForAction(driver1);
		PageObjectHelper pageObject2 = getPageObjectbjectForAction(driver2);

		// Creating threads
		Thread thread1 = new Thread(new ActionsInSameTime(pageObject1));
		Thread thread2 = new Thread(new ActionsInSameTime(pageObject2));

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

		private PageObjectHelper pageObject;

		public ActionsInSameTime(PageObjectHelper pageObject) {
			this.pageObject = pageObject;
		}

		public void run() {
			if (runInParallel(this.pageObject)) {
				resulteGetted = true;
			}
		}

	}

}
