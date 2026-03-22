package com.parabank.automation.utils;

import com.aventstack.extentreports.Status;
import com.parabank.automation.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class ApiLoggingUtils {

	private static final Logger LOGGER = LogManager.getLogger(ApiLoggingUtils.class);
	private static final int BODY_LOG_LIMIT = 3000;
	private static final List<String> SENSITIVE_HEADERS = List.of("authorization", "cookie", "set-cookie");

	private ApiLoggingUtils() {
	}

	public static void logRequest(String method, URI uri, Map<String, String> headers) {
		String message = new StringBuilder().append("API Request -> Method: ").append(method).append(" | URI: ")
				.append(uri).append(" | Headers: ").append(formatRequestHeaders(headers)).toString();

		LOGGER.info(message);
		ExtentTestManager.info(message);
	}

	public static void logResponse(String method, URI uri, HttpResponse<String> response, Duration duration) {
		String responseMessage = new StringBuilder().append("API Response <- Method: ").append(method)
				.append(" | URI: ").append(uri).append(" | Status Code: ").append(response.statusCode())
				.append(" | Duration: ").append(duration.toMillis()).append(" ms").append(" | Headers: ")
				.append(formatResponseHeaders(response.headers())).toString();

		LOGGER.info(responseMessage);
		ExtentTestManager.info(responseMessage);

		String responseBodyMessage = "API Response Body <- " + abbreviate(response.body());
		if (response.statusCode() >= 400) {
			LOGGER.error(responseBodyMessage);
			ExtentTestManager.log(Status.FAIL, responseBodyMessage);
		} else {
			LOGGER.info(responseBodyMessage);
			ExtentTestManager.info(responseBodyMessage);
		}
	}

	public static RuntimeException logFailureAndBuildException(String message, Throwable throwable) {
		LOGGER.error(message, throwable);
		ExtentTestManager.log(Status.FAIL, message);
		if (throwable != null) {
			ExtentTestManager.log(Status.FAIL, throwable);
		}
		return new RuntimeException(message, throwable);
	}

	public static RuntimeException logFailureAndBuildException(String message) {
		LOGGER.error(message);
		ExtentTestManager.log(Status.FAIL, message);
		return new RuntimeException(message);
	}

	private static String formatRequestHeaders(Map<String, String> headers) {
		if (headers == null || headers.isEmpty()) {
			return "{}";
		}

		Map<String, String> sanitizedHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		headers.forEach((key, value) -> sanitizedHeaders.put(key, sanitizeHeaderValue(key, value)));
		return sanitizedHeaders.toString();
	}

	private static String formatResponseHeaders(HttpHeaders httpHeaders) {
		if (httpHeaders == null || httpHeaders.map().isEmpty()) {
			return "{}";
		}

		Map<String, List<String>> sanitizedHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		httpHeaders.map().forEach((key, value) -> {
			List<String> sanitizedValues = new ArrayList<>();
			for (String headerValue : value) {
				sanitizedValues.add(sanitizeHeaderValue(key, headerValue));
			}
			sanitizedHeaders.put(key, sanitizedValues);
		});

		return sanitizedHeaders.toString();
	}

	private static String sanitizeHeaderValue(String headerName, String value) {
		if (value == null) {
			return null;
		}

		for (String sensitiveHeader : SENSITIVE_HEADERS) {
			if (sensitiveHeader.equalsIgnoreCase(headerName)) {
				return "***MASKED***";
			}
		}

		return value;
	}

	private static String abbreviate(String value) {
		if (value == null) {
			return "null";
		}

		String normalizedValue = value.replaceAll("\\s+", " ").trim();
		if (normalizedValue.length() <= BODY_LOG_LIMIT) {
			return normalizedValue;
		}

		return normalizedValue.substring(0, BODY_LOG_LIMIT) + "... [truncated]";
	}
}