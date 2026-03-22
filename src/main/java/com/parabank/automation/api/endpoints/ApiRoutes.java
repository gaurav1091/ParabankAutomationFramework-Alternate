package com.parabank.automation.api.endpoints;

public final class ApiRoutes {

	private static final String CUSTOMER_ACCOUNTS_ROUTE = "/customers/%d/accounts";

	private ApiRoutes() {
	}

	public static String getCustomerAccountsRoute(int customerId) {
		return String.format(CUSTOMER_ACCOUNTS_ROUTE, customerId);
	}
}