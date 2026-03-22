package com.parabank.automation.utils;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.enums.WaitStrategy;
import com.parabank.automation.exceptions.ElementOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ElementUtils {

	private final WaitUtils waitUtils;
	private final JavaScriptUtils javaScriptUtils;
	private static final Logger LOGGER = LogManager.getLogger(ElementUtils.class);

	public ElementUtils() {
		this.waitUtils = new WaitUtils();
		this.javaScriptUtils = new JavaScriptUtils();
	}

	public void click(By locator, WaitStrategy waitStrategy) {
		click(locator, waitStrategy, new By[0]);
	}

	public void click(By locator, WaitStrategy waitStrategy, By... fallbackLocators) {
		try {
			WebElement element = resolveElement(locator, waitStrategy, fallbackLocators);
			javaScriptUtils.scrollIntoView(element);

			try {
				element.click();
			} catch (ElementClickInterceptedException | StaleElementReferenceException exception) {
				WebElement refreshedElement = resolveElement(locator, waitStrategy, fallbackLocators);
				javaScriptUtils.scrollIntoView(refreshedElement);
				javaScriptUtils.clickUsingJavaScript(refreshedElement);
			}
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to click on element located by: " + locator, exception);
		}
	}

	public void click(WebElement element, WaitStrategy waitStrategy) {
		try {
			WebElement webElement = waitUtils.waitForElement(element, waitStrategy);
			javaScriptUtils.scrollIntoView(webElement);

			try {
				webElement.click();
			} catch (ElementClickInterceptedException | StaleElementReferenceException exception) {
				WebElement refreshedElement = waitUtils.waitForElement(element, waitStrategy);
				javaScriptUtils.scrollIntoView(refreshedElement);
				javaScriptUtils.clickUsingJavaScript(refreshedElement);
			}
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to click on WebElement.", exception);
		}
	}

	public void type(By locator, String value, WaitStrategy waitStrategy) {
		type(locator, value, waitStrategy, new By[0]);
	}

	public void type(By locator, String value, WaitStrategy waitStrategy, By... fallbackLocators) {
		try {
			WebElement element = resolveElement(locator, waitStrategy, fallbackLocators);
			javaScriptUtils.scrollIntoView(element);
			element.clear();
			element.sendKeys(value);

			String actualValue = element.getAttribute("value");
			if (actualValue == null || !actualValue.equals(value)) {
				javaScriptUtils.scrollIntoView(element);
				javaScriptUtils.clickUsingJavaScript(element);
				element.clear();
				element.sendKeys(value);
			}
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to type into element located by: " + locator, exception);
		}
	}

	public void type(WebElement element, String value, WaitStrategy waitStrategy) {
		try {
			WebElement webElement = waitUtils.waitForElement(element, waitStrategy);
			javaScriptUtils.scrollIntoView(webElement);
			webElement.clear();
			webElement.sendKeys(value);

			String actualValue = webElement.getAttribute("value");
			if (actualValue == null || !actualValue.equals(value)) {
				javaScriptUtils.scrollIntoView(webElement);
				webElement.clear();
				webElement.sendKeys(value);
			}
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to type into WebElement.", exception);
		}
	}

	public String getText(By locator, WaitStrategy waitStrategy) {
		return getText(locator, waitStrategy, new By[0]);
	}

	public String getText(By locator, WaitStrategy waitStrategy, By... fallbackLocators) {
		try {
			WebElement element = resolveElement(locator, waitStrategy, fallbackLocators);
			return element.getText().trim();
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to get text from element located by: " + locator, exception);
		}
	}

	public String getText(WebElement element, WaitStrategy waitStrategy) {
		try {
			WebElement webElement = waitUtils.waitForElement(element, waitStrategy);
			return webElement.getText().trim();
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to get text from WebElement.", exception);
		}
	}

	public boolean isDisplayed(By locator, WaitStrategy waitStrategy) {
		return isDisplayed(locator, waitStrategy, new By[0]);
	}

	public boolean isDisplayed(By locator, WaitStrategy waitStrategy, By... fallbackLocators) {
		try {
			WebElement element = resolveElement(locator, waitStrategy, fallbackLocators);
			return element != null && element.isDisplayed();
		} catch (Exception exception) {
			return false;
		}
	}

	public boolean isDisplayed(WebElement element, WaitStrategy waitStrategy) {
		try {
			WebElement webElement = waitUtils.waitForElement(element, waitStrategy);
			return webElement.isDisplayed();
		} catch (Exception exception) {
			return false;
		}
	}

	public void selectByVisibleText(WebElement element, String visibleText) {
		try {
			WebElement dropdown = waitUtils.waitForElement(element, WaitStrategy.VISIBLE);
			Select select = new Select(dropdown);
			select.selectByVisibleText(visibleText);
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to select dropdown option: " + visibleText, exception);
		}
	}

	public void jsClick(WebElement element) {
		try {
			javaScriptUtils.clickUsingJavaScript(element);
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to perform JavaScript click.", exception);
		}
	}

	public void scrollIntoView(WebElement element) {
		try {
			javaScriptUtils.scrollIntoView(element);
		} catch (Exception exception) {
			throw new ElementOperationException("Failed to scroll element into view.", exception);
		}
	}

	private WebElement resolveElement(By primaryLocator, WaitStrategy waitStrategy, By... fallbackLocators) {
		int retries = ConfigManager.getInstance().getResilientFindRetries();
		int delayMillis = ConfigManager.getInstance().getResilientFindDelayMillis();

		By[] allLocators = buildLocatorArray(primaryLocator, fallbackLocators);

		for (int attempt = 0; attempt <= retries; attempt++) {
			for (By locator : allLocators) {
				try {
					WebElement element = waitUtils.waitForElement(locator, waitStrategy);
					if (element != null) {
						return element;
					}
				} catch (Exception ignored) {
				}
			}

			if (attempt < retries) {
				LOGGER.warn("Resilient locator retry {} triggered for locator: {}", attempt + 1, primaryLocator);
				sleep(delayMillis);
			}
		}

		throw new ElementOperationException("Unable to resolve element. Primary locator: " + primaryLocator);
	}

	private By[] buildLocatorArray(By primaryLocator, By... fallbackLocators) {
		By[] allLocators = new By[1 + fallbackLocators.length];
		allLocators[0] = primaryLocator;

		for (int index = 0; index < fallbackLocators.length; index++) {
			allLocators[index + 1] = fallbackLocators[index];
		}

		return allLocators;
	}

	private void sleep(int delayMillis) {
		try {
			Thread.sleep(delayMillis);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
		}
	}
}