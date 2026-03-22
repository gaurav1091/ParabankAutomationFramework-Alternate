package com.parabank.automation.api.client;

import com.parabank.automation.api.base.BaseApiClient;
import com.parabank.automation.api.endpoints.ApiRoutes;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ParabankApiClient extends BaseApiClient {

	public HttpResponse<String> getAccountsByCustomerId(int customerId) {
		return sendGet(ApiRoutes.getCustomerAccountsRoute(customerId));
	}

	public HttpResponse<String> getAccountsByCustomerId(int customerId, String cookieHeader) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Cookie", cookieHeader);
		return sendGet(ApiRoutes.getCustomerAccountsRoute(customerId), headers);
	}
}