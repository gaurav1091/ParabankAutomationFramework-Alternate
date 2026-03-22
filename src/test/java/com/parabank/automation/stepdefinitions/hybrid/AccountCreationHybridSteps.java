package com.parabank.automation.stepdefinitions.hybrid;

import com.parabank.automation.api.services.AccountService;
import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.context.ScenarioContext;
import com.parabank.automation.dataproviders.HybridUiToApiAccountCreationTestDataProvider;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.dataproviders.OpenNewAccountTestDataProvider;
import com.parabank.automation.enums.ContextKey;
import com.parabank.automation.models.HybridUiToApiAccountCreationTestData;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.models.OpenNewAccountTestData;
import com.parabank.automation.pages.AccountsOverviewPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.pages.OpenNewAccountPage;
import com.parabank.automation.utils.CookieUtils;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AccountCreationHybridSteps {

	private final ScenarioContext scenarioContext;
	private final AccountService accountService;

	private LoginPage loginPage;
	private HomePage homePage;
	private AccountsOverviewPage accountsOverviewPage;
	private OpenNewAccountPage openNewAccountPage;

	public AccountCreationHybridSteps() {
		this.scenarioContext = ContextObjectManager.getScenarioContext();
		this.accountService = new AccountService();
	}

	@io.cucumber.java.en.Given("user logs in for hybrid account creation using key {string}")
	public void userLogsInForHybridAccountCreationUsingKey(String hybridDataKey) {
		HybridUiToApiAccountCreationTestData hybridData = HybridUiToApiAccountCreationTestDataProvider
				.getByKey(hybridDataKey);

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey(hybridData.getLoginKey());

		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());
		UiAssertions.assertTrue(homePage.isHomePageDisplayed(),
				"Home page is not displayed after hybrid account creation login.");

		scenarioContext.set(ContextKey.COOKIE_HEADER, CookieUtils.getCookieHeader());
		scenarioContext.set(ContextKey.NEW_ACCOUNT_ID, null);
		scenarioContext.set(ContextKey.CUSTOMER_ID, null);
	}

	@io.cucumber.java.en.When("user captures current account details from UI and API before account creation")
	public void userCapturesCurrentAccountDetailsFromUiAndApiBeforeAccountCreation() {
		accountsOverviewPage = homePage.clickAccountsOverview();

		UiAssertions.assertTrue(accountsOverviewPage.isAccountsOverviewPageDisplayed(),
				"Accounts Overview page is not displayed before account creation.");

		int customerId = accountsOverviewPage.extractCustomerIdFromPageSource();
		String cookieHeader = (String) scenarioContext.get(ContextKey.COOKIE_HEADER);

		List<String> preUiAccountIds = new ArrayList<>(accountsOverviewPage.getDisplayedAccountNumbers());

		HttpResponse<String> response = accountService.getAccountsByCustomerId(customerId, cookieHeader);
		List<String> preApiAccountIds = accountService.getAccountIdsByCustomerId(customerId, cookieHeader);

		UiAssertions.assertEquals(String.valueOf(response.statusCode()), "200",
				"Accounts API status code before account creation is incorrect.");
		UiAssertions.assertTrue(!preUiAccountIds.isEmpty(), "No UI account ids were captured before account creation.");
		UiAssertions.assertTrue(!preApiAccountIds.isEmpty(),
				"No API account ids were captured before account creation.");

		scenarioContext.set(ContextKey.CUSTOMER_ID, customerId);
		scenarioContext.set(ContextKey.PRE_UI_ACCOUNT_IDS, preUiAccountIds);
		scenarioContext.set(ContextKey.PRE_API_ACCOUNT_IDS, preApiAccountIds);
	}

	@io.cucumber.java.en.When("user creates a new account in UI using hybrid account creation key {string}")
	public void userCreatesANewAccountInUiUsingHybridAccountCreationKey(String hybridDataKey) {
		HybridUiToApiAccountCreationTestData hybridData = HybridUiToApiAccountCreationTestDataProvider
				.getByKey(hybridDataKey);

		OpenNewAccountTestData accountTestData = OpenNewAccountTestDataProvider
				.getOpenNewAccountTestDataByKey(hybridData.getOpenNewAccountKey());

		openNewAccountPage = homePage.clickOpenNewAccount();

		UiAssertions.assertTrue(openNewAccountPage.isOpenNewAccountPageDisplayed(),
				"Open New Account page is not displayed in hybrid account creation flow.");

		openNewAccountPage = openNewAccountPage.openNewAccount(accountTestData.getAccountType());

		UiAssertions.assertTrue(openNewAccountPage.isAccountCreationSuccessful(),
				"New account was not created successfully in hybrid flow.");
		UiAssertions.assertTrue(openNewAccountPage.isNewAccountNumberDisplayed(),
				"New account number is not displayed after hybrid account creation.");

		String newAccountId = openNewAccountPage.getNewAccountNumber();
		UiAssertions.assertNotEmpty(newAccountId, "New account id is empty after hybrid account creation.");

		scenarioContext.set(ContextKey.NEW_ACCOUNT_ID, newAccountId);
	}

	@io.cucumber.java.en.When("user refreshes account details from UI and API after account creation")
	public void userRefreshesAccountDetailsFromUiAndApiAfterAccountCreation() {
		int customerId = (Integer) scenarioContext.get(ContextKey.CUSTOMER_ID);
		String cookieHeader = (String) scenarioContext.get(ContextKey.COOKIE_HEADER);

		accountsOverviewPage = new HomePage().clickAccountsOverview();

		UiAssertions.assertTrue(accountsOverviewPage.isAccountsOverviewPageDisplayed(),
				"Accounts Overview page is not displayed after account creation.");

		List<String> postUiAccountIds = new ArrayList<>(accountsOverviewPage.getDisplayedAccountNumbers());

		HttpResponse<String> response = accountService.getAccountsByCustomerId(customerId, cookieHeader);
		List<String> postApiAccountIds = accountService.getAccountIdsByCustomerId(customerId, cookieHeader);

		UiAssertions.assertEquals(String.valueOf(response.statusCode()), "200",
				"Accounts API status code after account creation is incorrect.");
		UiAssertions.assertTrue(!postUiAccountIds.isEmpty(), "No UI account ids were captured after account creation.");
		UiAssertions.assertTrue(!postApiAccountIds.isEmpty(),
				"No API account ids were captured after account creation.");

		scenarioContext.set(ContextKey.POST_UI_ACCOUNT_IDS, postUiAccountIds);
		scenarioContext.set(ContextKey.POST_API_ACCOUNT_IDS, postApiAccountIds);
	}

	@io.cucumber.java.en.Then("the new account should be present in both UI and API account lists")
	public void theNewAccountShouldBePresentInBothUiAndApiAccountLists() {
		String newAccountId = (String) scenarioContext.get(ContextKey.NEW_ACCOUNT_ID);

		@SuppressWarnings("unchecked")
		List<String> postUiAccountIds = (List<String>) scenarioContext.get(ContextKey.POST_UI_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> postApiAccountIds = (List<String>) scenarioContext.get(ContextKey.POST_API_ACCOUNT_IDS);

		UiAssertions.assertNotEmpty(newAccountId, "New account id is not available in scenario context.");
		UiAssertions.assertTrue(postUiAccountIds.contains(newAccountId),
				"New account id is not present in UI account list after account creation.");
		UiAssertions.assertTrue(postApiAccountIds.contains(newAccountId),
				"New account id is not present in API account list after account creation.");
	}

	@io.cucumber.java.en.Then("account count should increase by one in both UI and API")
	public void accountCountShouldIncreaseByOneInBothUiAndApi() {
		@SuppressWarnings("unchecked")
		List<String> preUiAccountIds = (List<String>) scenarioContext.get(ContextKey.PRE_UI_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> postUiAccountIds = (List<String>) scenarioContext.get(ContextKey.POST_UI_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> preApiAccountIds = (List<String>) scenarioContext.get(ContextKey.PRE_API_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> postApiAccountIds = (List<String>) scenarioContext.get(ContextKey.POST_API_ACCOUNT_IDS);

		UiAssertions.assertEquals(String.valueOf(postUiAccountIds.size()), String.valueOf(preUiAccountIds.size() + 1),
				"UI account count did not increase by exactly one after account creation.");

		UiAssertions.assertEquals(String.valueOf(postApiAccountIds.size()), String.valueOf(preApiAccountIds.size() + 1),
				"API account count did not increase by exactly one after account creation.");
	}
}