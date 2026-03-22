package com.parabank.automation.config;

public final class FrameworkConstants {

	private FrameworkConstants() {
	}

	public static final String CONFIG_CLASSPATH_ROOT = "config/";
	public static final String TEST_DATA_CLASSPATH_ROOT = "testdata/";
	public static final String SCHEMA_CLASSPATH_ROOT = "schemas/";

	public static final String FRAMEWORK_CONFIG_FILE = "framework.properties";

	public static final String QA_CONFIG_FILE = "qa.properties";
	public static final String STAGE_CONFIG_FILE = "stage.properties";
	public static final String DEV_CONFIG_FILE = "dev.properties";

	public static final String REPORTS_FOLDER = "test-output/reports/";
	public static final String SCREENSHOTS_FOLDER = "test-output/screenshots/";
	public static final String CUCUMBER_REPORTS_FOLDER = "test-output/cucumber/";
	public static final String CUCUMBER_JSON_REPORT_FILE = CUCUMBER_REPORTS_FOLDER + "cucumber.json";
	public static final String CUCUMBER_JUNIT_REPORT_FILE = CUCUMBER_REPORTS_FOLDER + "cucumber.xml";
	public static final String CUCUMBER_HTML_REPORT_FILE = CUCUMBER_REPORTS_FOLDER + "cucumber.html";

	public static final String CHROME = "chrome";
	public static final String FIREFOX = "firefox";
	public static final String EDGE = "edge";

	public static final int DEFAULT_EXPLICIT_WAIT = 20;
	public static final int DEFAULT_IMPLICIT_WAIT = 5;
	public static final int DEFAULT_PAGE_LOAD_TIMEOUT = 30;
	public static final int DEFAULT_SCRIPT_TIMEOUT = 30;

	public static final int DEFAULT_SMART_WAIT_POLLING_MILLIS = 500;
	public static final int DEFAULT_RESILIENT_FIND_RETRIES = 2;
	public static final int DEFAULT_RESILIENT_FIND_DELAY_MILLIS = 300;

	public static final int DEFAULT_API_CONNECT_TIMEOUT_SECONDS = 15;
	public static final int DEFAULT_API_READ_TIMEOUT_SECONDS = 30;

	public static final boolean DEFAULT_STARTUP_VALIDATION_ENABLED = true;
	public static final int DEFAULT_STARTUP_VALIDATION_TIMEOUT_SECONDS = 10;
}