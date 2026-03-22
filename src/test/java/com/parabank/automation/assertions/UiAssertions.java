package com.parabank.automation.assertions;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.reports.ExtentTestManager;
import com.parabank.automation.reports.ReportPathManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class UiAssertions {

	private UiAssertions() {
	}

	public static void assertTrue(boolean condition, String failureMessage) {
		String successMessage = buildSuccessMessage("ASSERT TRUE", "Condition evaluated to true.");
		String standardizedFailureMessage = buildFailureMessage("ASSERT TRUE", failureMessage, "true",
				String.valueOf(condition));

		if (condition) {
			logPass(successMessage);
			return;
		}

		handleFailure(standardizedFailureMessage);
		Assert.fail(standardizedFailureMessage);
	}

	public static void assertFalse(boolean condition, String failureMessage) {
		String successMessage = buildSuccessMessage("ASSERT FALSE", "Condition evaluated to false.");
		String standardizedFailureMessage = buildFailureMessage("ASSERT FALSE", failureMessage, "false",
				String.valueOf(condition));

		if (!condition) {
			logPass(successMessage);
			return;
		}

		handleFailure(standardizedFailureMessage);
		Assert.fail(standardizedFailureMessage);
	}

	public static void assertEquals(String actual, String expected, String failureMessage) {
		boolean condition = expected == null ? actual == null : expected.equals(actual);

		String successMessage = buildSuccessMessage("ASSERT EQUALS",
				"Expected and actual values matched. Expected=[" + expected + "], Actual=[" + actual + "]");
		String standardizedFailureMessage = buildFailureMessage("ASSERT EQUALS", failureMessage,
				String.valueOf(expected), String.valueOf(actual));

		if (condition) {
			logPass(successMessage);
			return;
		}

		handleFailure(standardizedFailureMessage);
		Assert.fail(standardizedFailureMessage);
	}

	public static void assertContains(String actual, String expectedSubstring, String failureMessage) {
		String nullFailureMessage = buildFailureMessage("ASSERT CONTAINS", "Actual value is null.", expectedSubstring,
				"null");

		if (actual == null) {
			handleFailure(nullFailureMessage);
			Assert.fail(nullFailureMessage);
		}

		boolean condition = actual.contains(expectedSubstring);

		String successMessage = buildSuccessMessage("ASSERT CONTAINS",
				"Actual value contains expected substring. Expected substring=[" + expectedSubstring + "], Actual=["
						+ actual + "]");
		String standardizedFailureMessage = buildFailureMessage("ASSERT CONTAINS", failureMessage, expectedSubstring,
				actual);

		if (condition) {
			logPass(successMessage);
			return;
		}

		handleFailure(standardizedFailureMessage);
		Assert.fail(standardizedFailureMessage);
	}

	public static void assertNotEmpty(String actual, String failureMessage) {
		String nullFailureMessage = buildFailureMessage("ASSERT NOT EMPTY", "Actual value is null.", "non-empty value",
				"null");

		if (actual == null) {
			handleFailure(nullFailureMessage);
			Assert.fail(nullFailureMessage);
		}

		boolean condition = !actual.trim().isEmpty();

		String successMessage = buildSuccessMessage("ASSERT NOT EMPTY",
				"Actual value is not empty. Actual=[" + actual + "]");
		String standardizedFailureMessage = buildFailureMessage("ASSERT NOT EMPTY", failureMessage, "non-empty value",
				actual);

		if (condition) {
			logPass(successMessage);
			return;
		}

		handleFailure(standardizedFailureMessage);
		Assert.fail(standardizedFailureMessage);
	}

	private static String buildSuccessMessage(String assertionType, String detail) {
		return "[" + assertionType + " PASSED] " + detail;
	}

	private static String buildFailureMessage(String assertionType, String failureMessage, String expected,
			String actual) {
		return "[" + assertionType + " FAILED] " + failureMessage + " | Expected=[" + expected + "] | Actual=[" + actual
				+ "]";
	}

	private static void logPass(String message) {
		ExtentTestManager.log(Status.PASS, message);
	}

	private static void handleFailure(String failureMessage) {
		ExtentTestManager.log(Status.FAIL, failureMessage);
		attachFailureScreenshotIfPossible("assertion_failure");
	}

	private static void attachFailureScreenshotIfPossible(String screenshotNamePrefix) {
		try {
			if (!ConfigManager.getInstance().isScreenshotOnFailEnabled()) {
				return;
			}

			if (DriverManager.getDriver() == null) {
				return;
			}

			String screenshotPath = captureScreenshot(screenshotNamePrefix);

			if (ExtentTestManager.hasTest()) {
				ExtentTestManager.getTest().fail("Assertion failure screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		} catch (Exception exception) {
			ExtentTestManager.log(Status.WARNING,
					"Unable to attach assertion failure screenshot. Reason: " + exception.getMessage());
		}
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