package com.parabank.automation.utils;

import com.parabank.automation.driver.DriverManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;
import java.util.stream.Collectors;

public final class CookieUtils {

	private CookieUtils() {
	}

	public static String getCookieHeader() {
		WebDriver driver = DriverManager.getDriver();

		if (driver == null) {
			throw new RuntimeException("WebDriver is null. Unable to build cookie header.");
		}

		Set<Cookie> cookies = driver.manage().getCookies();

		if (cookies == null || cookies.isEmpty()) {
			throw new RuntimeException("No browser cookies found. Unable to build authenticated cookie header.");
		}

		return cookies.stream().map(cookie -> cookie.getName() + "=" + cookie.getValue())
				.collect(Collectors.joining("; "));
	}
}