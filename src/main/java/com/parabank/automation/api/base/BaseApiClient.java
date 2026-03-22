package com.parabank.automation.api.base;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.utils.ApiLoggingUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

public class BaseApiClient {

	private final HttpClient httpClient;

	public BaseApiClient() {
		this.httpClient = ApiRequestSpecification.buildHttpClient();
	}

	protected HttpResponse<String> sendGet(String relativePath) {
		return sendGet(relativePath, null);
	}

	protected HttpResponse<String> sendGet(String relativePath, Map<String, String> headers) {
		Map<String, String> requestHeaders = headers == null ? Collections.emptyMap() : headers;
		URI uri = ApiRequestSpecification.buildUri(relativePath);

		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri)
				.timeout(Duration.ofSeconds(ConfigManager.getInstance().getApiReadTimeoutSeconds())).GET();

		requestHeaders.forEach((key, value) -> {
			if (key != null && value != null && !value.trim().isEmpty()) {
				requestBuilder.header(key, value);
			}
		});

		HttpRequest request = requestBuilder.build();
		ApiLoggingUtils.logRequest("GET", uri, requestHeaders);
		Instant startTime = Instant.now();

		try {
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			ApiLoggingUtils.logResponse("GET", uri, response, Duration.between(startTime, Instant.now()));
			return response;
		} catch (IOException exception) {
			throw ApiLoggingUtils.logFailureAndBuildException(
					"GET API call failed for URI: " + uri + " | Reason: " + exception.getMessage(), exception);
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
			throw ApiLoggingUtils.logFailureAndBuildException(
					"GET API call interrupted for URI: " + uri + " | Reason: " + exception.getMessage(), exception);
		}
	}
}