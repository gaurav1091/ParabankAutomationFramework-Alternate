package com.parabank.automation.utils;

import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class FailureDiagnosticsUtils {

	private static final Logger LOGGER = LogManager.getLogger(FailureDiagnosticsUtils.class);
	private static final int PAGE_SOURCE_LOG_LIMIT = 2500;
	private static final int MAX_BROWSER_LOG_ENTRIES = 10;

	private FailureDiagnosticsUtils() {
	}

	public static void logUiFailureDetails(String scenarioName) {
		if (!DriverManager.hasDriver()) {
			String message = "UI failure diagnostics skipped because WebDriver is not available.";
			LOGGER.warn(message);
			ExtentTestManager.warning(message);
			return;
		}

		WebDriver driver = DriverManager.getDriver();
		logBasicBrowserState(driver, scenarioName);
		logPageSourceSnippet(driver);
		logBrowserConsoleEntries(driver);
	}

	private static void logBasicBrowserState(WebDriver driver, String scenarioName) {
		try {
			String currentUrl = driver.getCurrentUrl();
			String title = driver.getTitle();
			String readyState = getDocumentReadyState(driver);
			Set<String> windowHandles = driver.getWindowHandles();

			String message = new StringBuilder().append("UI Failure Diagnostics -> Scenario: ").append(scenarioName)
					.append(" | Current URL: ").append(currentUrl).append(" | Title: ").append(title)
					.append(" | Document Ready State: ").append(readyState).append(" | Window Count: ")
					.append(windowHandles.size()).toString();

			LOGGER.error(message);
			ExtentTestManager.fail(message);
		} catch (Exception exception) {
			String message = "Unable to capture basic browser state for failure diagnostics.";
			LOGGER.error(message, exception);
			ExtentTestManager.fail(message + " Reason: " + exception.getMessage());
		}
	}

	private static void logPageSourceSnippet(WebDriver driver) {
		try {
			String pageSource = driver.getPageSource();
			if (pageSource == null || pageSource.trim().isEmpty()) {
				ExtentTestManager.warning("UI Failure Diagnostics -> Page source is empty.");
				return;
			}

			String normalizedSource = pageSource.replaceAll("\\s+", " ").trim();
			String snippet = normalizedSource.length() <= PAGE_SOURCE_LOG_LIMIT ? normalizedSource
					: normalizedSource.substring(0, PAGE_SOURCE_LOG_LIMIT) + "... [truncated]";

			String message = "UI Failure Diagnostics -> Page Source Snippet: " + snippet;
			LOGGER.error(message);
			ExtentTestManager.fail(message);
		} catch (Exception exception) {
			String message = "Unable to capture page source snippet for failure diagnostics.";
			LOGGER.error(message, exception);
			ExtentTestManager.fail(message + " Reason: " + exception.getMessage());
		}
	}

	private static void logBrowserConsoleEntries(WebDriver driver) {
		try {
			LogEntries browserLogs = driver.manage().logs().get(LogType.BROWSER);
			List<String> consoleEntries = new ArrayList<>();

			for (LogEntry logEntry : browserLogs) {
				consoleEntries.add(logEntry.getLevel() + " | " + logEntry.getMessage());
				if (consoleEntries.size() >= MAX_BROWSER_LOG_ENTRIES) {
					break;
				}
			}

			if (consoleEntries.isEmpty()) {
				ExtentTestManager.info("UI Failure Diagnostics -> Browser console logs are empty or unavailable.");
				return;
			}

			for (String consoleEntry : consoleEntries) {
				String message = "UI Failure Diagnostics -> Browser Console: " + consoleEntry;
				LOGGER.error(message);
				ExtentTestManager.fail(message);
			}
		} catch (Exception exception) {
			String message = "Unable to capture browser console logs for failure diagnostics.";
			LOGGER.warn(message, exception);
			ExtentTestManager.warning(message + " Reason: " + exception.getMessage());
		}
	}

	private static String getDocumentReadyState(WebDriver driver) {
		try {
			Object readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState");
			return readyState == null ? "unknown" : readyState.toString();
		} catch (Exception exception) {
			return "unavailable";
		}
	}
}