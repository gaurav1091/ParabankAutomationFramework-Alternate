package com.parabank.automation.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

	private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

	private DriverManager() {
	}

	public static WebDriver getDriver() {
		return DRIVER_THREAD_LOCAL.get();
	}

	public static void setDriver(WebDriver driver) {
		DRIVER_THREAD_LOCAL.set(driver);
	}

	public static boolean hasDriver() {
		return DRIVER_THREAD_LOCAL.get() != null;
	}

	public static void quitAndRemove() {
		WebDriver driver = DRIVER_THREAD_LOCAL.get();

		try {
			if (driver != null) {
				driver.quit();
			}
		} finally {
			DRIVER_THREAD_LOCAL.remove();
		}
	}

	public static void unload() {
		DRIVER_THREAD_LOCAL.remove();
	}
}