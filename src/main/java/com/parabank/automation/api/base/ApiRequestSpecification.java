package com.parabank.automation.api.base;

import com.parabank.automation.config.ConfigManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;

public final class ApiRequestSpecification {

	private ApiRequestSpecification() {
	}

	public static HttpClient buildHttpClient() {
		return HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(ConfigManager.getInstance().getApiConnectTimeoutSeconds())).build();
	}

	public static URI buildUri(String relativePath) {
		String baseUrl = ConfigManager.getInstance().getApiBaseUrl();

		String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
		String normalizedRelativePath = relativePath.startsWith("/") ? relativePath : "/" + relativePath;

		return URI.create(normalizedBaseUrl + normalizedRelativePath);
	}
}