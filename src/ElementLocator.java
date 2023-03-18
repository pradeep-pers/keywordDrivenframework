import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ElementLocator {
	public static WebDriverWait wait;
	
	public static WebElement elementExists(String by, String locator) {

		WebElement element = null;
		Boolean flag = true;

		if (by.equalsIgnoreCase("className")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.className(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("cssSelector")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.cssSelector(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("id")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.id(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("linkText")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.linkText(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("name")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.name(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("partialLinkText")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.partialLinkText(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("tagName")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.tagName(locator));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("xpath")) {
			try {
				element = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.xpath(locator));
				System.out.println("element found");
			} catch (NoSuchElementException ex) {
				flag = false;
			}

		} else {
			flag = false;
		}

		if (flag) {

			System.out.println("Found the element  : with locator=" + by + " & value=" + locator);

		} else {
			System.out.println("Not able to find element : with locator=" + by + " & value=" + locator);

		}

		return element;

	}

	
	public static WebElement waitElementExists(String by, String locator) {

		WebElement element = null;
		Boolean flag = true;
		wait = new WebDriverWait(TestSuitRunner.driver.get(TestSuitRunner.i),10);
		
		if (by.equalsIgnoreCase("className")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("cssSelector")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("id")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("linkText")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("name")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("partialLinkText")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("tagName")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}
		}
		if (by.equalsIgnoreCase("xpath")) {
			try {
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
			} catch (NoSuchElementException ex) {
				flag = false;
			}

		} else {
			flag = false;
		}

		if (flag) {

			System.out.println("Found the element  : with locator=" + by + " & value=" + locator);

		} else {
			System.out.println("Not able to find element : with locator=" + by + " & value=" + locator);

		}

		return element;

	}
}
