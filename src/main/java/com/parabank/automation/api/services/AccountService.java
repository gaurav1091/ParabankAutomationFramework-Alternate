package com.parabank.automation.api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parabank.automation.api.client.ParabankApiClient;
import com.parabank.automation.utils.ApiLoggingUtils;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountService {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final ParabankApiClient parabankApiClient;

	public AccountService() {
		this.parabankApiClient = new ParabankApiClient();
	}

	public HttpResponse<String> getAccountsByCustomerId(int customerId) {
		return parabankApiClient.getAccountsByCustomerId(customerId);
	}

	public HttpResponse<String> getAccountsByCustomerId(int customerId, String cookieHeader) {
		return parabankApiClient.getAccountsByCustomerId(customerId, cookieHeader);
	}

	public List<Map<String, Object>> getAccountsAsMapsByCustomerId(int customerId) {
		HttpResponse<String> response = getAccountsByCustomerId(customerId);
		return extractAccounts(response);
	}

	public List<Map<String, Object>> getAccountsAsMapsByCustomerId(int customerId, String cookieHeader) {
		HttpResponse<String> response = getAccountsByCustomerId(customerId, cookieHeader);
		return extractAccounts(response);
	}

	public List<String> getAccountIdsByCustomerId(int customerId) {
		List<Map<String, Object>> accounts = getAccountsAsMapsByCustomerId(customerId);
		return extractAccountIds(accounts);
	}

	public List<String> getAccountIdsByCustomerId(int customerId, String cookieHeader) {
		List<Map<String, Object>> accounts = getAccountsAsMapsByCustomerId(customerId, cookieHeader);
		return extractAccountIds(accounts);
	}

	private List<Map<String, Object>> extractAccounts(HttpResponse<String> response) {
		if (response.statusCode() != 200) {
			throw ApiLoggingUtils.logFailureAndBuildException("Accounts API returned non-200 status. Status: "
					+ response.statusCode() + " | Body: " + response.body());
		}

		try {
			return OBJECT_MAPPER.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {
			});
		} catch (Exception exception) {
			throw ApiLoggingUtils.logFailureAndBuildException(
					"Failed to parse accounts API response body. Body: " + response.body(), exception);
		}
	}

	private List<String> extractAccountIds(List<Map<String, Object>> accounts) {
		List<String> accountIds = new ArrayList<>();

		for (Map<String, Object> account : accounts) {
			if (account != null && account.get("id") != null) {
				accountIds.add(String.valueOf(account.get("id")));
			}
		}

		return accountIds;
	}
}