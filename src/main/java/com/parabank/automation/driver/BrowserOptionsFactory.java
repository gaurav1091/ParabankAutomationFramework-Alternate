package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class BrowserOptionsFactory {

	private BrowserOptionsFactory() {
	}

	public static ChromeOptions getChromeOptions() {
		ChromeOptions options = new ChromeOptions();

		options.addArguments("--start-maximized");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-notifications");

		if (ConfigManager.getInstance().isHeadless()) {
			options.addArguments("--headless=new");
		}

		return options;
	}

	public static FirefoxOptions getFirefoxOptions() {
		FirefoxOptions options = new FirefoxOptions();

		if (ConfigManager.getInstance().isHeadless()) {
			options.addArguments("--headless");
		}

		return options;
	}

	public static EdgeOptions getEdgeOptions() {
		EdgeOptions options = new EdgeOptions();

		options.addArguments("--start-maximized");
		options.addArguments("--disable-notifications");

		if (ConfigManager.getInstance().isHeadless()) {
			options.addArguments("--headless=new");
		}

		return options;
	}
}