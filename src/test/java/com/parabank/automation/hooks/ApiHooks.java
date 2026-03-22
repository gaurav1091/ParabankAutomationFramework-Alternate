package com.parabank.automation.hooks;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.context.ScenarioContext;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.enums.ContextKey;
import com.parabank.automation.pages.AccountsOverviewPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.reports.ExtentTestManager;
import com.parabank.automation.utils.CookieUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.List;

public class ApiHooks {

	@Before(value = "@api_authenticated", order = 2)
	public void prepareAuthenticatedApiSession(Scenario scenario) {
		if (!DriverManager.hasDriver()) {
			throw new IllegalStateException("Browser was not initialized for @api_authenticated scenario. "
					+ "Authenticated API setup requires a browser-backed session.");
		}

		ScenarioContext scenarioContext = ContextObjectManager.getScenarioContext();

		LoginPage loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(),
				"Login page is not displayed for authenticated API setup.");

		HomePage homePage = loginPage.loginToApplication(ConfigManager.getInstance().getUsername(),
				ConfigManager.getInstance().getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(),
				"Home page is not displayed after API authentication login.");

		AccountsOverviewPage accountsOverviewPage = homePage.clickAccountsOverview();
		UiAssertions.assertTrue(accountsOverviewPage.isAccountsOverviewPageDisplayed(),
				"Accounts Overview page is not displayed during authenticated API setup.");

		int customerId = accountsOverviewPage.extractCustomerIdFromPageSource();
		List<String> uiAccountIds = accountsOverviewPage.getDisplayedAccountNumbers();
		String cookieHeader = CookieUtils.getCookieHeader();

		scenarioContext.set(ContextKey.CUSTOMER_ID, customerId);
		scenarioContext.set(ContextKey.UI_ACCOUNT_IDS, uiAccountIds);
		scenarioContext.set(ContextKey.COOKIE_HEADER, cookieHeader);

		ExtentTestManager.info("Authenticated API session prepared successfully using browser-backed login.");
		ExtentTestManager.info("Captured customer id for API scenario: " + customerId);
		ExtentTestManager.info("Captured UI account count for API scenario: " + uiAccountIds.size());
	}

	@After(value = "@api", order = 2)
	public void afterApiScenario(Scenario scenario) {
		ExtentTestManager.info("API scenario cleanup completed.");
	}
}