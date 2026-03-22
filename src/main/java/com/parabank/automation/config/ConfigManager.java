package com.parabank.automation.config;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class ConfigManager {

	private static volatile ConfigManager instance;

	private final Properties frameworkProperties;
	private final Properties environmentProperties;

	private ConfigManager() {
		frameworkProperties = PropertyReader
				.loadProperties(FrameworkConstants.CONFIG_CLASSPATH_ROOT + FrameworkConstants.FRAMEWORK_CONFIG_FILE);

		environmentProperties = PropertyReader
				.loadProperties(FrameworkConstants.CONFIG_CLASSPATH_ROOT + EnvironmentManager.getEnvironmentConfigFileName());
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
		return (executionMode == null || executionMode.trim().isEmpty()) ? FrameworkConstants.EXECUTION_MODE_LOCAL
				: executionMode.trim().toLowerCase();
	}

	public boolean isRemoteExecution() {
		return FrameworkConstants.EXECUTION_MODE_REMOTE.equalsIgnoreCase(getExecutionMode());
	}

	public String getRemoteProvider() {
		String remoteProvider = getProperty("remote.provider");
		return (remoteProvider == null || remoteProvider.trim().isEmpty())
				? FrameworkConstants.REMOTE_PROVIDER_SELENIUM_GRID
				: remoteProvider.trim().toLowerCase();
	}

	public boolean isSeleniumGridExecution() {
		return isRemoteExecution() && FrameworkConstants.REMOTE_PROVIDER_SELENIUM_GRID.equalsIgnoreCase(getRemoteProvider());
	}

	public boolean isBrowserStackExecution() {
		return isRemoteExecution() && FrameworkConstants.REMOTE_PROVIDER_BROWSERSTACK.equalsIgnoreCase(getRemoteProvider());
	}

	public String getSeleniumRemoteUrl() {
		String remoteUrl = getProperty("selenium.remote.url");
		return (remoteUrl == null || remoteUrl.trim().isEmpty()) ? FrameworkConstants.DEFAULT_SELENIUM_REMOTE_URL
				: remoteUrl.trim();
	}

	public String getBrowserStackHubUrl() {
		String hubUrl = getProperty("browserstack.hub.url");
		return (hubUrl == null || hubUrl.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_HUB_URL
				: hubUrl.trim();
	}

	public String getBrowserStackUsername() {
		return SensitiveDataResolver.resolveCredentialValue(getProperty("browserstack.username"),
				"BrowserStack username");
	}

	public String getBrowserStackAccessKey() {
		return SensitiveDataResolver.resolveCredentialValue(getProperty("browserstack.access.key"),
				"BrowserStack access key");
	}

	public String getBrowserStackRemoteUrl() {
		URI hubUri = URI.create(getBrowserStackHubUrl());
		String encodedUsername = URLEncoder.encode(getBrowserStackUsername(), StandardCharsets.UTF_8);
		String encodedAccessKey = URLEncoder.encode(getBrowserStackAccessKey(), StandardCharsets.UTF_8);
		String userInfo = encodedUsername + ":" + encodedAccessKey;

		URI authenticatedUri = URI.create(hubUri.getScheme() + "://" + userInfo + "@"
				+ hubUri.getAuthority().replaceFirst("^.*@", "") + hubUri.getPath());

		return authenticatedUri.toString();
	}

	public String getBrowserStackOs() {
		String os = getProperty("browserstack.os");
		return (os == null || os.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_OS : os.trim();
	}

	public String getBrowserStackOsVersion() {
		String osVersion = getProperty("browserstack.os.version");
		return (osVersion == null || osVersion.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_OS_VERSION
				: osVersion.trim();
	}

	public String getBrowserStackBrowserVersion() {
		String browserVersion = getProperty("browserstack.browser.version");
		return (browserVersion == null || browserVersion.trim().isEmpty())
				? FrameworkConstants.DEFAULT_BROWSERSTACK_BROWSER_VERSION
				: browserVersion.trim();
	}

	public String getBrowserStackProjectName() {
		String projectName = getProperty("browserstack.project.name");
		return (projectName == null || projectName.trim().isEmpty())
				? FrameworkConstants.DEFAULT_BROWSERSTACK_PROJECT_NAME
				: projectName.trim();
	}

	public String getBrowserStackBuildName() {
		String buildName = getProperty("browserstack.build.name");
		return (buildName == null || buildName.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_BUILD_NAME
				: buildName.trim();
	}

	public String getBrowserStackSessionName() {
		String sessionName = getProperty("browserstack.session.name");
		return (sessionName == null || sessionName.trim().isEmpty())
				? FrameworkConstants.DEFAULT_BROWSERSTACK_SESSION_NAME
				: sessionName.trim();
	}

	public boolean isBrowserStackLocalEnabled() {
		String value = getProperty("browserstack.local");
		return (value == null || value.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_LOCAL
				: Boolean.parseBoolean(value.trim());
	}

	public boolean isBrowserStackDebugEnabled() {
		String value = getProperty("browserstack.debug");
		return (value == null || value.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_DEBUG
				: Boolean.parseBoolean(value.trim());
	}

	public boolean isBrowserStackNetworkLogsEnabled() {
		String value = getProperty("browserstack.network.logs");
		return (value == null || value.trim().isEmpty()) ? FrameworkConstants.DEFAULT_BROWSERSTACK_NETWORK_LOGS
				: Boolean.parseBoolean(value.trim());
	}

	public String getBrowserStackConsoleLogs() {
		String consoleLogs = getProperty("browserstack.console.logs");
		return (consoleLogs == null || consoleLogs.trim().isEmpty())
				? FrameworkConstants.DEFAULT_BROWSERSTACK_CONSOLE_LOGS
				: consoleLogs.trim();
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
