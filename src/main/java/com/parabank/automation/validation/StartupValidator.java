package com.parabank.automation.validation;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.config.EnvironmentManager;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.enums.EnvironmentType;
import com.parabank.automation.exceptions.StartupValidationException;
import com.parabank.automation.reports.ReportPathManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class StartupValidator {

	private static final Logger LOGGER = LogManager.getLogger(StartupValidator.class);

	private StartupValidator() {
	}

	public static void validateOrThrow() {
		ConfigManager configManager = ConfigManager.getInstance();

		if (!configManager.isStartupValidationEnabled()) {
			LOGGER.info("Startup validation is disabled by configuration.");
			return;
		}

		validateEnvironment();
		validateBrowser(configManager.getBrowser());
		validateExecutionMode(configManager.getExecutionMode());
		validateUri("base.url", configManager.getBaseUrl());
		validateUri("api.base.url", configManager.getApiBaseUrl());

		if (configManager.isRemoteExecution()) {
			validateUri("selenium.remote.url", configManager.getSeleniumRemoteUrl());
			validateEndpointReachability("Selenium Grid", buildSeleniumStatusUrl(configManager.getSeleniumRemoteUrl()),
					configManager.getStartupValidationTimeoutSeconds());
		}

		validatePositive("implicit.wait", configManager.getImplicitWait());
		validatePositive("explicit.wait", configManager.getExplicitWait());
		validatePositive("page.load.timeout", configManager.getPageLoadTimeout());
		validatePositive("script.timeout", configManager.getScriptTimeout());
		validatePositive("thread.count", configManager.getThreadCount());
		validatePositive("data.provider.thread.count", configManager.getDataProviderThreadCount());
		validatePositive("api.connect.timeout.seconds", configManager.getApiConnectTimeoutSeconds());
		validatePositive("api.read.timeout.seconds", configManager.getApiReadTimeoutSeconds());
		validatePositive("startup.validation.timeout.seconds", configManager.getStartupValidationTimeoutSeconds());

		validateEndpointReachability("Application Base URL", configManager.getBaseUrl(),
				configManager.getStartupValidationTimeoutSeconds());
		validateEndpointReachability("API Base URL", configManager.getApiBaseUrl(),
				configManager.getStartupValidationTimeoutSeconds());

		validateReportDirectories();

		LOGGER.info("Startup validation completed successfully.");
	}

	private static void validateEnvironment() {
		try {
			EnvironmentType environmentType = EnvironmentManager.getEnvironmentType();
			LOGGER.info("Validated environment: {}", environmentType);
		} catch (Exception exception) {
			throw new StartupValidationException("Invalid environment configuration. Supported values: qa, stage, dev.",
					exception);
		}
	}

	private static void validateBrowser(String browser) {
		if (browser == null || browser.trim().isEmpty()) {
			throw new StartupValidationException("Browser configuration is missing.");
		}

		String normalizedBrowser = browser.trim().toLowerCase();
		if (!FrameworkConstants.CHROME.equals(normalizedBrowser)
				&& !FrameworkConstants.FIREFOX.equals(normalizedBrowser)
				&& !FrameworkConstants.EDGE.equals(normalizedBrowser)) {
			throw new StartupValidationException(
					"Unsupported browser configured: " + browser + ". Supported values: chrome, firefox, edge.");
		}

		LOGGER.info("Validated browser: {}", normalizedBrowser);
	}

	private static void validateExecutionMode(String executionMode) {
		if (executionMode == null || executionMode.trim().isEmpty()) {
			throw new StartupValidationException("Execution mode configuration is missing.");
		}

		String normalizedExecutionMode = executionMode.trim().toLowerCase();
		if (!"local".equals(normalizedExecutionMode) && !"remote".equals(normalizedExecutionMode)) {
			throw new StartupValidationException(
					"Unsupported execution mode: " + executionMode + ". Supported values: local, remote.");
		}

		LOGGER.info("Validated execution mode: {}", normalizedExecutionMode);
	}

	private static void validateUri(String propertyName, String uriValue) {
		if (uriValue == null || uriValue.trim().isEmpty()) {
			throw new StartupValidationException("Required configuration is missing: " + propertyName);
		}

		try {
			URI uri = URI.create(uriValue.trim());
			if (uri.getScheme() == null || uri.getHost() == null) {
				throw new StartupValidationException("Invalid URI configured for " + propertyName + ": " + uriValue);
			}
		} catch (IllegalArgumentException exception) {
			throw new StartupValidationException("Invalid URI configured for " + propertyName + ": " + uriValue,
					exception);
		}

		LOGGER.info("Validated URI for property: {}", propertyName);
	}

	private static void validatePositive(String propertyName, int value) {
		if (value <= 0) {
			throw new StartupValidationException("Configuration value must be greater than zero for property: "
					+ propertyName + " | Actual: " + value);
		}

		LOGGER.info("Validated positive numeric configuration: {}={}", propertyName, value);
	}

	private static void validateEndpointReachability(String endpointName, String url, int timeoutSeconds) {
		try {
			HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(timeoutSeconds)).build();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.timeout(Duration.ofSeconds(timeoutSeconds)).GET().build();

			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

			LOGGER.info("Validated endpoint reachability: {} -> HTTP {}", endpointName, response.statusCode());
		} catch (Exception exception) {
			throw new StartupValidationException(
					"Failed startup reachability validation for " + endpointName + " at URL: " + url, exception);
		}
	}

	private static String buildSeleniumStatusUrl(String seleniumRemoteUrl) {
		String trimmedUrl = seleniumRemoteUrl.trim();

		if (trimmedUrl.endsWith("/wd/hub")) {
			return trimmedUrl.substring(0, trimmedUrl.length() - "/wd/hub".length()) + "/status";
		}

		if (trimmedUrl.endsWith("/")) {
			return trimmedUrl + "status";
		}

		return trimmedUrl + "/status";
	}

	private static void validateReportDirectories() {
		try {
			ReportPathManager.createDirectoryIfNotExists(FrameworkConstants.REPORTS_FOLDER);
			ReportPathManager.createDirectoryIfNotExists(FrameworkConstants.SCREENSHOTS_FOLDER);
			ReportPathManager.createDirectoryIfNotExists(FrameworkConstants.CUCUMBER_REPORTS_FOLDER);
		} catch (Exception exception) {
			throw new StartupValidationException("Failed to create or validate report directories.", exception);
		}

		LOGGER.info("Validated report directories.");
	}
}