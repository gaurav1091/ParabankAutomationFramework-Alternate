package com.parabank.automation.utils;

import com.aventstack.extentreports.Status;
import com.parabank.automation.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FrameworkLogger {

	private FrameworkLogger() {
	}

	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	public static void info(Logger logger, String message) {
		logger.info(message);
		ExtentTestManager.log(Status.INFO, message);
	}

	public static void warn(Logger logger, String message) {
		logger.warn(message);
		ExtentTestManager.log(Status.WARNING, message);
	}

	public static void error(Logger logger, String message, Throwable throwable) {
		logger.error(message, throwable);
		ExtentTestManager.log(Status.FAIL, message);
		if (throwable != null) {
			ExtentTestManager.log(Status.FAIL, throwable);
		}
	}

	public static void debug(Logger logger, String message) {
		logger.debug(message);
	}
}