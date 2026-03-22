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
		String remoteUrl = ConfigManager.getInstance().getSeleniumRemoteUrl();

		try {
			switch (browserType) {
			case CHROME:
				return new RemoteWebDriver(new URL(remoteUrl), BrowserOptionsFactory.getChromeOptions());

			case FIREFOX:
				return new RemoteWebDriver(new URL(remoteUrl), BrowserOptionsFactory.getFirefoxOptions());

			case EDGE:
				return new RemoteWebDriver(new URL(remoteUrl), BrowserOptionsFactory.getEdgeOptions());

			default:
				throw new DriverInitializationException(
						"Unsupported browser type for remote execution: " + browserType);
			}
		} catch (MalformedURLException exception) {
			throw new DriverInitializationException("Invalid Selenium remote URL: " + remoteUrl, exception);
		}
	}
}