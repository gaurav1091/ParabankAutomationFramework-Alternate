package com.parabank.automation.utils;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.enums.WaitStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.Arrays;

public class WaitUtils {

	private static final Logger LOGGER = LogManager.getLogger(WaitUtils.class);

	private final WebDriver driver;
	private final FluentWait<WebDriver> wait;

	public WaitUtils() {
		this.driver = DriverManager.getDriver();
		this.wait = (driver == null) ? null
				: new FluentWait<>(driver)
						.withTimeout(Duration.ofSeconds(ConfigManager.getInstance().getExplicitWait()))
						.pollingEvery(Duration.ofMillis(ConfigManager.getInstance().getSmartWaitPollingMillis()))
						.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class);
	}

	public WebElement waitForElement(By locator, WaitStrategy waitStrategy) {
		try {
			return getWait().until(webDriver -> findElementByStrategy(locator, waitStrategy));
		} catch (TimeoutException exception) {
			throw new RuntimeException(
					"Element was not found using wait strategy " + waitStrategy + " for locator: " + locator,
					exception);
		}
	}

	public WebElement waitForAnyVisible(By... locators) {
		try {
			return getWait().until(webDriver -> {
				for (By locator : locators) {
					try {
						WebElement element = webDriver.findElement(locator);
						if (element.isDisplayed()) {
							return element;
						}
					} catch (Exception ignored) {
					}
				}
				return null;
			});
		} catch (TimeoutException exception) {
			throw new RuntimeException("None of the provided locators became visible: " + Arrays.toString(locators),
					exception);
		}
	}

	public boolean waitForAnyVisibleSafely(By... locators) {
		try {
			waitForAnyVisible(locators);
			return true;
		} catch (Exception exception) {
			LOGGER.debug("None of the locators became visible: {}", Arrays.toString(locators));
			return false;
		}
	}

	public WebElement waitForElement(WebElement element, WaitStrategy waitStrategy) {
		try {
			return getWait().until(webDriver -> {
				switch (waitStrategy) {
				case CLICKABLE:
					return (element != null && element.isDisplayed() && element.isEnabled()) ? element : null;
				case VISIBLE:
				case PRESENCE:
					return (element != null && element.isDisplayed()) ? element : null;
				case NONE:
				default:
					return element;
				}
			});
		} catch (TimeoutException exception) {
			throw new RuntimeException("WebElement was not found using wait strategy: " + waitStrategy, exception);
		}
	}

	public boolean waitForTitleContains(String titleFraction) {
		return waitForCondition(
				webDriver -> webDriver.getTitle() != null && webDriver.getTitle().contains(titleFraction));
	}

	public boolean waitForUrlContains(String urlFraction) {
		return waitForCondition(
				webDriver -> webDriver.getCurrentUrl() != null && webDriver.getCurrentUrl().contains(urlFraction));
	}

	public boolean waitForTextPresentInElement(WebElement element, String expectedText) {
		return waitForCondition(webDriver -> {
			try {
				return element != null && element.getText() != null && element.getText().contains(expectedText);
			} catch (Exception exception) {
				return false;
			}
		});
	}

	public boolean waitForTextPresentInElementLocated(By locator, String expectedText) {
		return waitForCondition(webDriver -> {
			try {
				WebElement element = webDriver.findElement(locator);
				return element.getText() != null && element.getText().contains(expectedText);
			} catch (Exception exception) {
				return false;
			}
		});
	}

	public boolean waitForVisibilityOf(By locator) {
		return waitForCondition(webDriver -> {
			try {
				WebElement element = webDriver.findElement(locator);
				return element.isDisplayed();
			} catch (Exception exception) {
				return false;
			}
		});
	}

	public boolean waitForClickabilityOf(By locator) {
		return waitForCondition(webDriver -> {
			try {
				WebElement element = webDriver.findElement(locator);
				return element.isDisplayed() && element.isEnabled();
			} catch (Exception exception) {
				return false;
			}
		});
	}

	public boolean waitForInvisibilityOf(By locator) {
		return waitForCondition(webDriver -> {
			try {
				WebElement element = webDriver.findElement(locator);
				return !element.isDisplayed();
			} catch (Exception exception) {
				return true;
			}
		});
	}

	public boolean waitForPageLoadComplete() {
		return waitForCondition(webDriver -> {
			try {
				Object readyState = ((JavascriptExecutor) webDriver).executeScript("return document.readyState");
				return readyState != null && "complete".equalsIgnoreCase(readyState.toString());
			} catch (Exception exception) {
				return false;
			}
		});
	}

	public boolean waitForAjaxToComplete() {
		return waitForCondition(webDriver -> {
			try {
				Object result = ((JavascriptExecutor) webDriver)
						.executeScript("return (window.jQuery != null) ? jQuery.active == 0 : true;");
				return result instanceof Boolean && (Boolean) result;
			} catch (Exception exception) {
				return true;
			}
		});
	}

	public boolean waitForPageToBeStable() {
		boolean pageLoaded = waitForPageLoadComplete();
		boolean ajaxComplete = waitForAjaxToComplete();
		return pageLoaded && ajaxComplete;
	}

	private WebElement findElementByStrategy(By locator, WaitStrategy waitStrategy) {
		WebElement element = getDriver().findElement(locator);

		switch (waitStrategy) {
		case CLICKABLE:
			return (element.isDisplayed() && element.isEnabled()) ? element : null;
		case VISIBLE:
		case PRESENCE:
			return element.isDisplayed() ? element : null;
		case NONE:
		default:
			return element;
		}
	}

	private boolean waitForCondition(ExpectedCondition<Boolean> condition) {
		try {
			Boolean result = getWait().until(condition);
			return Boolean.TRUE.equals(result);
		} catch (TimeoutException exception) {
			return false;
		}
	}

	private WebDriver getDriver() {
		if (driver == null) {
			throw new IllegalStateException("WebDriver is null. WaitUtils cannot be used without an active driver.");
		}
		return driver;
	}

	private FluentWait<WebDriver> getWait() {
		if (wait == null) {
			throw new IllegalStateException(
					"WaitUtils cannot be used because WebDriver is not initialized for the current thread.");
		}
		return wait;
	}
}