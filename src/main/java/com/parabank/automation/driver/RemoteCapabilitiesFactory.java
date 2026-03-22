package com.parabank.automation.driver;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.enums.BrowserType;
import org.openqa.selenium.MutableCapabilities;

import java.util.HashMap;
import java.util.Map;

public final class RemoteCapabilitiesFactory {

	private RemoteCapabilitiesFactory() {
	}

	public static MutableCapabilities getSeleniumGridCapabilities(BrowserType browserType) {
		switch (browserType) {
		case CHROME:
			return BrowserOptionsFactory.getChromeOptions();
		case FIREFOX:
			return BrowserOptionsFactory.getFirefoxOptions();
		case EDGE:
			return BrowserOptionsFactory.getEdgeOptions();
		default:
			throw new IllegalArgumentException("Unsupported browser type for Selenium Grid: " + browserType);
		}
	}

	public static MutableCapabilities getBrowserStackCapabilities(BrowserType browserType) {
		ConfigManager configManager = ConfigManager.getInstance();

		MutableCapabilities capabilities = new MutableCapabilities();
		capabilities.setCapability("browserName", toBrowserStackBrowserName(browserType));
		capabilities.setCapability("browserVersion", configManager.getBrowserStackBrowserVersion());

		Map<String, Object> browserStackOptions = new HashMap<>();
		browserStackOptions.put("os", configManager.getBrowserStackOs());
		browserStackOptions.put("osVersion", configManager.getBrowserStackOsVersion());
		browserStackOptions.put("projectName", configManager.getBrowserStackProjectName());
		browserStackOptions.put("buildName", configManager.getBrowserStackBuildName());
		browserStackOptions.put("sessionName", configManager.getBrowserStackSessionName());
		browserStackOptions.put("local", configManager.isBrowserStackLocalEnabled());
		browserStackOptions.put("debug", configManager.isBrowserStackDebugEnabled());
		browserStackOptions.put("networkLogs", configManager.isBrowserStackNetworkLogsEnabled());
		browserStackOptions.put("consoleLogs", configManager.getBrowserStackConsoleLogs());
		browserStackOptions.put("source", "parabank-automation-framework");

		capabilities.setCapability("bstack:options", browserStackOptions);
		return capabilities;
	}

	private static String toBrowserStackBrowserName(BrowserType browserType) {
		switch (browserType) {
		case CHROME:
			return "Chrome";
		case FIREFOX:
			return "Firefox";
		case EDGE:
			return "Edge";
		default:
			throw new IllegalArgumentException("Unsupported browser type for BrowserStack: " + browserType);
		}
	}
}
