package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.enums.BrowserType;
import com.parabank.automation.exceptions.DriverInitializationException;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public final class DriverFactory {

	private DriverFactory() {
	}

	public static WebDriver initializeDriver() {
		if (DriverManager.hasDriver()) {
			return DriverManager.getDriver();
		}

		String browserName = ConfigManager.getInstance().getBrowser();

		if (browserName == null || browserName.trim().isEmpty()) {
			throw new DriverInitializationException("Browser is not configured. Please set 'browser' in properties.");
		}

		BrowserType browserType;
		try {
			browserType = BrowserType.valueOf(browserName.trim().toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new DriverInitializationException("Invalid browser configured: " + browserName, exception);
		}

		WebDriver driver = BrowserFactory.createDriver(browserType);
		DriverManager.setDriver(driver);
		applyDriverSettings(driver);

		return driver;
	}

	private static void applyDriverSettings(WebDriver driver) {
		if (driver == null) {
			throw new DriverInitializationException("WebDriver is null. Cannot apply driver settings.");
		}

		ConfigManager configManager = ConfigManager.getInstance();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configManager.getImplicitWait()));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(configManager.getPageLoadTimeout()));
		driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(configManager.getScriptTimeout()));

		if (!configManager.isHeadless() && !configManager.isBrowserStackExecution()) {
			driver.manage().window().maximize();
		}
	}

	public static void quitDriver() {
		DriverManager.quitAndRemove();
	}
}
