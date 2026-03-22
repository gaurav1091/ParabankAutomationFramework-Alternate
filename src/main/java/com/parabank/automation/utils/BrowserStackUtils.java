package com.parabank.automation.utils;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.driver.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public final class BrowserStackUtils {

	private static final Logger LOGGER = LogManager.getLogger(BrowserStackUtils.class);

	private BrowserStackUtils() {
	}

	public static void setSessionName(String scenarioName) {
		if (!ConfigManager.getInstance().isBrowserStackExecution() || !DriverManager.hasDriver()) {
			return;
		}

		String command = "browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\": \""
				+ escapeForJson(scenarioName) + "\"}}";

		executeBrowserStackCommand(command);
	}

	public static void setSessionStatus(boolean passed, String reason) {
		if (!ConfigManager.getInstance().isBrowserStackExecution() || !DriverManager.hasDriver()) {
			return;
		}

		String status = passed ? "passed" : "failed";
		String command = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\""
				+ status + "\", \"reason\": \"" + escapeForJson(reason) + "\"}}";

		executeBrowserStackCommand(command);
	}

	private static void executeBrowserStackCommand(String command) {
		try {
			WebDriver driver = DriverManager.getDriver();
			if (driver instanceof JavascriptExecutor) {
				((JavascriptExecutor) driver).executeScript(command);
			}
		} catch (Exception exception) {
			LOGGER.warn("Failed to execute BrowserStack session command: {}", exception.getMessage());
		}
	}

	private static String escapeForJson(String value) {
		if (value == null) {
			return "";
		}

		return value.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}
