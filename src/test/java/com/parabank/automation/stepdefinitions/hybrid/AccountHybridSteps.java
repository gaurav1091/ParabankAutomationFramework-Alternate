package com.parabank.automation.stepdefinitions.hybrid;

import com.parabank.automation.api.assertions.ApiAssertions;
import com.parabank.automation.api.services.AccountService;
import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.context.ScenarioContext;
import com.parabank.automation.dataproviders.HybridAccountValidationTestDataProvider;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.enums.ContextKey;
import com.parabank.automation.models.HybridAccountValidationTestData;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.pages.AccountsOverviewPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.utils.CookieUtils;

import java.net.http.HttpResponse;
import java.util.List;

public class AccountHybridSteps {

	private final ScenarioContext scenarioContext;
	private final AccountService accountService;

	private LoginPage loginPage;
	private HomePage homePage;
	private AccountsOverviewPage accountsOverviewPage;

	public AccountHybridSteps() {
		this.scenarioContext = ContextObjectManager.getScenarioContext();
		this.accountService = new AccountService();
	}

	@io.cucumber.java.en.Given("user performs hybrid login with test data key {string}")
	public void userPerformsHybridLoginWithTestDataKey(String hybridTestDataKey) {
		HybridAccountValidationTestData hybridData = HybridAccountValidationTestDataProvider
				.getHybridAccountValidationTestDataByKey(hybridTestDataKey);

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey(hybridData.getLoginKey());

		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());
		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after hybrid login.");

		String cookieHeader = CookieUtils.getCookieHeader();
		scenarioContext.set(ContextKey.COOKIE_HEADER, cookieHeader);
	}

	@io.cucumber.java.en.When("user opens accounts overview and captures account details from UI")
	public void userOpensAccountsOverviewAndCapturesAccountDetailsFromUi() {
		accountsOverviewPage = homePage.clickAccountsOverview();

		UiAssertions.assertTrue(accountsOverviewPage.isAccountsOverviewPageDisplayed(),
				"Accounts Overview page is not displayed in hybrid flow.");

		int customerId = accountsOverviewPage.extractCustomerIdFromPageSource();
		List<String> uiAccountIds = accountsOverviewPage.getDisplayedAccountNumbers();

		scenarioContext.set(ContextKey.CUSTOMER_ID, customerId);
		scenarioContext.set(ContextKey.UI_ACCOUNT_IDS, uiAccountIds);

		UiAssertions.assertTrue(!uiAccountIds.isEmpty(), "No account ids were captured from UI.");
	}

	@io.cucumber.java.en.When("user fetches account details from API for the same customer")
	public void userFetchesAccountDetailsFromApiForTheSameCustomer() {
		int customerId = (Integer) scenarioContext.get(ContextKey.CUSTOMER_ID);
		String cookieHeader = (String) scenarioContext.get(ContextKey.COOKIE_HEADER);

		HttpResponse<String> response = accountService.getAccountsByCustomerId(customerId, cookieHeader);
		List<String> apiAccountIds = accountService.getAccountIdsByCustomerId(customerId, cookieHeader);

		scenarioContext.set(ContextKey.API_STATUS_CODE, response.statusCode());
		scenarioContext.set(ContextKey.API_ACCOUNT_IDS, apiAccountIds);
	}

	@io.cucumber.java.en.Then("API and UI account numbers should match")
	public void apiAndUiAccountNumbersShouldMatch() {
		int statusCode = (Integer) scenarioContext.get(ContextKey.API_STATUS_CODE);

		@SuppressWarnings("unchecked")
		List<String> uiAccountIds = (List<String>) scenarioContext.get(ContextKey.UI_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> apiAccountIds = (List<String>) scenarioContext.get(ContextKey.API_ACCOUNT_IDS);

		ApiAssertions.assertStatusCode(statusCode, 200, "Accounts API status code mismatch.");
		ApiAssertions.assertListNotEmpty(apiAccountIds, "API returned an empty account list.");
		ApiAssertions.assertCollectionsMatchIgnoringOrder(apiAccountIds, uiAccountIds,
				"API and UI account id collections do not match.");
	}
}