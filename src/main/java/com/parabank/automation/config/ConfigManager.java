package com.parabank.automation.config;

import java.util.Properties;

public final class ConfigManager {

	private static volatile ConfigManager instance;

	private final Properties frameworkProperties;
	private final Properties environmentProperties;

	private ConfigManager() {
		frameworkProperties = PropertyReader
				.loadProperties(FrameworkConstants.CONFIG_CLASSPATH_ROOT + FrameworkConstants.FRAMEWORK_CONFIG_FILE);

		environmentProperties = PropertyReader.loadProperties(
				FrameworkConstants.CONFIG_CLASSPATH_ROOT + EnvironmentManager.getEnvironmentConfigFileName());
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null) {
					instance = new ConfigManager();
				}
			}
		}
		return instance;
	}

	public String getProperty(String key) {
		String systemValue = System.getProperty(key);
		if (systemValue != null && !systemValue.trim().isEmpty()) {
			return systemValue.trim();
		}

		String environmentValue = environmentProperties.getProperty(key);
		if (environmentValue != null && !environmentValue.trim().isEmpty()) {
			return environmentValue.trim();
		}

		String frameworkValue = frameworkProperties.getProperty(key);
		if (frameworkValue != null && !frameworkValue.trim().isEmpty()) {
			return frameworkValue.trim();
		}

		return null;
	}

	public String getBrowser() {
		return getProperty("browser");
	}

	public boolean isHeadless() {
		return Boolean.parseBoolean(getProperty("headless"));
	}

	public String getBaseUrl() {
		return getProperty("base.url");
	}

	public String getApiBaseUrl() {
		return getProperty("api.base.url");
	}

	public String getUsername() {
		return SensitiveDataResolver.resolveCredentialValue(getProperty("username"), "Config username property");
	}

	public String getPassword() {
		return SensitiveDataResolver.resolveCredentialValue(getProperty("password"), "Config password property");
	}

	public String getExecutionMode() {
		String executionMode = getProperty("execution.mode");
		return (executionMode == null || executionMode.trim().isEmpty()) ? "local" : executionMode.trim().toLowerCase();
	}

	public boolean isRemoteExecution() {
		return "remote".equalsIgnoreCase(getExecutionMode());
	}

	public String getSeleniumRemoteUrl() {
		String remoteUrl = getProperty("selenium.remote.url");
		return (remoteUrl == null || remoteUrl.trim().isEmpty()) ? "http://localhost:4444/wd/hub" : remoteUrl.trim();
	}

	public int getImplicitWait() {
		return getIntProperty("implicit.wait", FrameworkConstants.DEFAULT_IMPLICIT_WAIT);
	}

	public int getExplicitWait() {
		return getIntProperty("explicit.wait", FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
	}

	public int getPageLoadTimeout() {
		return getIntProperty("page.load.timeout", FrameworkConstants.DEFAULT_PAGE_LOAD_TIMEOUT);
	}

	public int getScriptTimeout() {
		return getIntProperty("script.timeout", FrameworkConstants.DEFAULT_SCRIPT_TIMEOUT);
	}

	public boolean isHighlightElementsEnabled() {
		return Boolean.parseBoolean(getProperty("highlight.elements"));
	}

	public boolean isScreenshotOnPassEnabled() {
		return Boolean.parseBoolean(getProperty("take.screenshot.on.pass"));
	}

	public boolean isScreenshotOnFailEnabled() {
		return Boolean.parseBoolean(getProperty("take.screenshot.on.fail"));
	}

	public int getThreadCount() {
		return getIntProperty("thread.count", 3);
	}

	public int getDataProviderThreadCount() {
		return getIntProperty("data.provider.thread.count", 3);
	}

	public String getParallelMode() {
		String parallelMode = getProperty("parallel.mode");
		return (parallelMode == null || parallelMode.trim().isEmpty()) ? "methods" : parallelMode.trim().toLowerCase();
	}

	public boolean isRetryEnabled() {
		return Boolean.parseBoolean(getProperty("retry.enabled"));
	}

	public int getRetryCount() {
		return getIntProperty("retry.count", 0);
	}

	public int getSmartWaitPollingMillis() {
		return getIntProperty("smart.wait.polling.millis", FrameworkConstants.DEFAULT_SMART_WAIT_POLLING_MILLIS);
	}

	public int getResilientFindRetries() {
		return getIntProperty("resilient.find.retries", FrameworkConstants.DEFAULT_RESILIENT_FIND_RETRIES);
	}

	public int getResilientFindDelayMillis() {
		return getIntProperty("resilient.find.delay.millis", FrameworkConstants.DEFAULT_RESILIENT_FIND_DELAY_MILLIS);
	}

	public int getApiConnectTimeoutSeconds() {
		return getIntProperty("api.connect.timeout.seconds", FrameworkConstants.DEFAULT_API_CONNECT_TIMEOUT_SECONDS);
	}

	public int getApiReadTimeoutSeconds() {
		return getIntProperty("api.read.timeout.seconds", FrameworkConstants.DEFAULT_API_READ_TIMEOUT_SECONDS);
	}

	public boolean isStartupValidationEnabled() {
		String value = getProperty("startup.validation.enabled");
		if (value == null || value.trim().isEmpty()) {
			return FrameworkConstants.DEFAULT_STARTUP_VALIDATION_ENABLED;
		}
		return Boolean.parseBoolean(value.trim());
	}

	public int getStartupValidationTimeoutSeconds() {
		return getIntProperty("startup.validation.timeout.seconds",
				FrameworkConstants.DEFAULT_STARTUP_VALIDATION_TIMEOUT_SECONDS);
	}

	private int getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);

		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}

		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException exception) {
			return defaultValue;
		}
	}
}