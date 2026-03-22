package com.parabank.automation.reports;

import com.parabank.automation.config.FrameworkConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ReportPathManager {

	private static final String REPORT_FILE_PREFIX = "ExtentReport_";
	private static final String REPORT_FILE_EXTENSION = ".html";
	private static final String LATEST_REPORT_FILE_NAME = "ExtentReport_Latest.html";

	private ReportPathManager() {
	}

	public static String getReportFilePath() {
		createDirectoryIfNotExists(FrameworkConstants.REPORTS_FOLDER);

		String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		return FrameworkConstants.REPORTS_FOLDER + REPORT_FILE_PREFIX + timeStamp + REPORT_FILE_EXTENSION;
	}

	public static String getLatestReportFilePath() {
		createDirectoryIfNotExists(FrameworkConstants.REPORTS_FOLDER);
		return FrameworkConstants.REPORTS_FOLDER + LATEST_REPORT_FILE_NAME;
	}

	public static String getCucumberJsonReportPath() {
		createDirectoryIfNotExists(FrameworkConstants.CUCUMBER_REPORTS_FOLDER);
		return FrameworkConstants.CUCUMBER_JSON_REPORT_FILE;
	}

	public static String getCucumberJunitReportPath() {
		createDirectoryIfNotExists(FrameworkConstants.CUCUMBER_REPORTS_FOLDER);
		return FrameworkConstants.CUCUMBER_JUNIT_REPORT_FILE;
	}

	public static String getCucumberHtmlReportPath() {
		createDirectoryIfNotExists(FrameworkConstants.CUCUMBER_REPORTS_FOLDER);
		return FrameworkConstants.CUCUMBER_HTML_REPORT_FILE;
	}

	public static void copyAsLatestReport(String sourceReportPath) {
		if (sourceReportPath == null || sourceReportPath.trim().isEmpty()) {
			return;
		}

		File sourceFile = new File(sourceReportPath);
		File latestFile = new File(getLatestReportFilePath());

		if (!sourceFile.exists()) {
			return;
		}

		try {
			Files.copy(sourceFile.toPath(), latestFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException exception) {
			throw new RuntimeException("Failed to create latest Extent report copy.", exception);
		}
	}

	public static void createDirectoryIfNotExists(String folderPath) {
		File directory = new File(folderPath);
		if (!directory.exists() && !directory.mkdirs()) {
			throw new RuntimeException("Failed to create directory: " + folderPath);
		}
	}
}