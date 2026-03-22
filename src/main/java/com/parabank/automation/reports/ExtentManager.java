package com.parabank.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.config.EnvironmentManager;

public final class ExtentManager {

	private static volatile ExtentReports extentReports;
	private static String reportFilePath;

	private ExtentManager() {
	}

	public static void initialize() {
		getOrCreateExtentReports();
	}

	public static ExtentTest createTest(String testName) {
		return getOrCreateExtentReports().createTest(testName);
	}

	public static String getReportFilePath() {
		return reportFilePath;
	}

	public static void flushReport() {
		if (extentReports != null) {
			synchronized (ExtentManager.class) {
				if (extentReports != null) {
					extentReports.flush();
					ReportPathManager.copyAsLatestReport(reportFilePath);
				}
			}
		}
	}

	private static ExtentReports getOrCreateExtentReports() {
		if (extentReports == null) {
			synchronized (ExtentManager.class) {
				if (extentReports == null) {
					extentReports = createInstance();
				}
			}
		}
		return extentReports;
	}

	private static ExtentReports createInstance() {
		reportFilePath = ReportPathManager.getReportFilePath();

		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFilePath);
		sparkReporter.config().setReportName("ParaBank Automation Execution Report");
		sparkReporter.config().setDocumentTitle("ParaBank Automation Report");
		sparkReporter.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");

		ExtentReports extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		extent.setSystemInfo("Framework", "Selenium Java Cucumber TestNG");
		extent.setSystemInfo("Environment", EnvironmentManager.getCurrentEnvironment());
		extent.setSystemInfo("Browser", ConfigManager.getInstance().getBrowser());
		extent.setSystemInfo("Headless", String.valueOf(ConfigManager.getInstance().isHeadless()));
		extent.setSystemInfo("Execution Mode", ConfigManager.getInstance().getExecutionMode());
		extent.setSystemInfo("Parallel Mode", ConfigManager.getInstance().getParallelMode());
		extent.setSystemInfo("Thread Count", String.valueOf(ConfigManager.getInstance().getThreadCount()));
		extent.setSystemInfo("Data Provider Thread Count",
				String.valueOf(ConfigManager.getInstance().getDataProviderThreadCount()));
		extent.setSystemInfo("Base URL", ConfigManager.getInstance().getBaseUrl());
		extent.setSystemInfo("API Base URL", ConfigManager.getInstance().getApiBaseUrl());
		extent.setSystemInfo("Selenium Remote URL", ConfigManager.getInstance().getSeleniumRemoteUrl());
		extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		extent.setSystemInfo("OS Name", System.getProperty("os.name"));
		extent.setSystemInfo("OS Version", System.getProperty("os.version"));
		extent.setSystemInfo("User Name", System.getProperty("user.name"));

		return extent;
	}
}