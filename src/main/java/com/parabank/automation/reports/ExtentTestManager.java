package com.parabank.automation.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentTestManager {

	private static final ThreadLocal<ExtentTest> EXTENT_TEST_THREAD_LOCAL = new ThreadLocal<>();

	private ExtentTestManager() {
	}

	public static void setTest(ExtentTest extentTest) {
		EXTENT_TEST_THREAD_LOCAL.set(extentTest);
	}

	public static ExtentTest getTest() {
		return EXTENT_TEST_THREAD_LOCAL.get();
	}

	public static boolean hasTest() {
		return EXTENT_TEST_THREAD_LOCAL.get() != null;
	}

	public static void log(Status status, String message) {
		if (hasTest()) {
			getTest().log(status, message);
		}
	}

	public static void log(Status status, Throwable throwable) {
		if (hasTest()) {
			getTest().log(status, throwable);
		}
	}

	public static void info(String message) {
		log(Status.INFO, message);
	}

	public static void pass(String message) {
		log(Status.PASS, message);
	}

	public static void fail(String message) {
		log(Status.FAIL, message);
	}

	public static void warning(String message) {
		log(Status.WARNING, message);
	}

	public static void attachScreenshot(String title, String screenshotPath) {
		try {
			if (hasTest() && screenshotPath != null && !screenshotPath.trim().isEmpty()) {
				getTest().addScreenCaptureFromPath(screenshotPath, title);
			}
		} catch (Exception exception) {
			log(Status.WARNING, "Unable to attach screenshot to report. Reason: " + exception.getMessage());
		}
	}

	public static void attachScreenshotForStatus(Status status, String title, String screenshotPath) {
		try {
			if (hasTest() && screenshotPath != null && !screenshotPath.trim().isEmpty()) {
				getTest().log(status, title).addScreenCaptureFromPath(screenshotPath);
			}
		} catch (Exception exception) {
			log(Status.WARNING, "Unable to attach screenshot to report. Reason: " + exception.getMessage());
		}
	}

	public static void captureAndAttachScreenshot(String screenshotName, Status status, String title) {
		try {
			if (DriverManager.getDriver() == null) {
				return;
			}

			String screenshotPath = captureScreenshot(screenshotName);
			attachScreenshotForStatus(status, title, screenshotPath);
		} catch (Exception exception) {
			log(Status.WARNING, "Unable to capture screenshot for report. Reason: " + exception.getMessage());
		}
	}

	public static void captureAndAttachFailureScreenshot(String screenshotName) {
		if (ConfigManager.getInstance().isScreenshotOnFailEnabled()) {
			captureAndAttachScreenshot(screenshotName, Status.FAIL, "Failure Screenshot");
		}
	}

	public static void captureAndAttachPassScreenshot(String screenshotName) {
		if (ConfigManager.getInstance().isScreenshotOnPassEnabled()) {
			captureAndAttachScreenshot(screenshotName, Status.PASS, "Pass Screenshot");
		}
	}

	public static void unload() {
		EXTENT_TEST_THREAD_LOCAL.remove();
	}

	private static String captureScreenshot(String screenshotName) {
		WebDriver driver = DriverManager.getDriver();

		if (driver == null) {
			throw new RuntimeException("WebDriver is null. Cannot capture screenshot.");
		}

		ReportPathManager.createDirectoryIfNotExists(FrameworkConstants.SCREENSHOTS_FOLDER);

		String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String destinationPath = FrameworkConstants.SCREENSHOTS_FOLDER + screenshotName + "_" + timeStamp + ".png";

		File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destinationFile = new File(destinationPath);

		try {
			Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception exception) {
			throw new RuntimeException("Failed to capture screenshot: " + destinationPath, exception);
		}

		return destinationPath;
	}
}