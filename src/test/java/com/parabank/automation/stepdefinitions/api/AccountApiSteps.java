package com.parabank.automation.stepdefinitions.api;

import com.parabank.automation.api.assertions.ApiAssertions;
import com.parabank.automation.api.services.AccountService;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.context.ScenarioContext;
import com.parabank.automation.enums.ContextKey;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class AccountApiSteps {

	private static final String ACCOUNTS_RESPONSE_SCHEMA = FrameworkConstants.SCHEMA_CLASSPATH_ROOT
			+ "accounts-response-schema.json";

	private final ScenarioContext scenarioContext;
	private final AccountService accountService;

	public AccountApiSteps() {
		this.scenarioContext = ContextObjectManager.getScenarioContext();
		this.accountService = new AccountService();
	}

	@io.cucumber.java.en.When("user requests account details through API for the authenticated customer")
	public void userRequestsAccountDetailsThroughApiForTheAuthenticatedCustomer() {
		int customerId = (Integer) scenarioContext.get(ContextKey.CUSTOMER_ID);
		String cookieHeader = (String) scenarioContext.get(ContextKey.COOKIE_HEADER);

		HttpResponse<String> response = accountService.getAccountsByCustomerId(customerId, cookieHeader);
		List<Map<String, Object>> apiAccounts = accountService.getAccountsAsMapsByCustomerId(customerId, cookieHeader);
		List<String> apiAccountIds = accountService.getAccountIdsByCustomerId(customerId, cookieHeader);

		scenarioContext.set(ContextKey.API_STATUS_CODE, response.statusCode());
		scenarioContext.set(ContextKey.API_ACCOUNTS, apiAccounts);
		scenarioContext.set(ContextKey.API_ACCOUNT_IDS, apiAccountIds);
	}

	@io.cucumber.java.en.Then("accounts API response status should be {int}")
	public void accountsApiResponseStatusShouldBe(int expectedStatusCode) {
		int actualStatusCode = (Integer) scenarioContext.get(ContextKey.API_STATUS_CODE);
		ApiAssertions.assertStatusCode(actualStatusCode, expectedStatusCode, "Accounts API status code mismatch.");
	}

	@io.cucumber.java.en.Then("accounts API response should contain at least one account")
	public void accountsApiResponseShouldContainAtLeastOneAccount() {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> apiAccounts = (List<Map<String, Object>>) scenarioContext
				.get(ContextKey.API_ACCOUNTS);

		ApiAssertions.assertListNotEmpty(apiAccounts, "Accounts API returned an empty account list.");
	}

	@io.cucumber.java.en.Then("each account should contain valid core details")
	public void eachAccountShouldContainValidCoreDetails() {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> apiAccounts = (List<Map<String, Object>>) scenarioContext
				.get(ContextKey.API_ACCOUNTS);

		ApiAssertions.assertJsonMatchesSchema(apiAccounts, ACCOUNTS_RESPONSE_SCHEMA,
				"Accounts API schema validation failed.");
		ApiAssertions.assertAllAccountsHaveValidCoreFields(apiAccounts,
				"Accounts API returned one or more invalid account objects.");
	}
}