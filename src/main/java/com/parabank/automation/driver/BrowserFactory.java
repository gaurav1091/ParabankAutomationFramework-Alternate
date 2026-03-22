package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.enums.BrowserType;
import com.parabank.automation.exceptions.DriverInitializationException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public final class BrowserFactory {

	private BrowserFactory() {
	}

	public static WebDriver createDriver(BrowserType browserType) {
		if (ConfigManager.getInstance().isRemoteExecution()) {
			return createRemoteDriver(browserType);
		}
		return createLocalDriver(browserType);
	}

	private static WebDriver createLocalDriver(BrowserType browserType) {
		switch (browserType) {
		case CHROME:
			WebDriverManager.chromedriver().setup();
			return new org.openqa.selenium.chrome.ChromeDriver(BrowserOptionsFactory.getChromeOptions());

		case FIREFOX:
			WebDriverManager.firefoxdriver().setup();
			return new org.openqa.selenium.firefox.FirefoxDriver(BrowserOptionsFactory.getFirefoxOptions());

		case EDGE:
			WebDriverManager.edgedriver().setup();
			return new org.openqa.selenium.edge.EdgeDriver(BrowserOptionsFactory.getEdgeOptions());

		default:
			throw new DriverInitializationException("Unsupported browser type: " + browserType);
		}
	}

	private static WebDriver createRemoteDriver(BrowserType browserType) {
		ConfigManager configManager = ConfigManager.getInstance();

		try {
			if (configManager.isBrowserStackExecution()) {
				return new RemoteWebDriver(new URL(configManager.getBrowserStackRemoteUrl()),
						RemoteCapabilitiesFactory.getBrowserStackCapabilities(browserType));
			}

			if (configManager.isSeleniumGridExecution()) {
				return new RemoteWebDriver(new URL(configManager.getSeleniumRemoteUrl()),
						RemoteCapabilitiesFactory.getSeleniumGridCapabilities(browserType));
			}

			throw new DriverInitializationException(
					"Unsupported remote provider configured: " + configManager.getRemoteProvider());
		} catch (MalformedURLException exception) {
			throw new DriverInitializationException("Invalid remote URL configured.", exception);
		}
	}
}
