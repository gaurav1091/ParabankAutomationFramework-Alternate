package com.parabank.automation.utils;

import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.reports.ReportPathManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

	private ScreenshotUtils() {
	}

	public static String captureScreenshot(String screenshotName) {
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
			FileUtils.copyFile(sourceFile, destinationFile);
		} catch (IOException exception) {
			throw new RuntimeException("Failed to capture screenshot: " + destinationPath, exception);
		}

		return destinationPath;
	}
}