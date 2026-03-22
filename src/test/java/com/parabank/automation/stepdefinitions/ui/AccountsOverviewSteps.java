package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.pages.AccountsOverviewPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountsOverviewSteps {

	private LoginPage loginPage;
	private HomePage homePage;
	private AccountsOverviewPage accountsOverviewPage;

	@Given("user is logged into the ParaBank application")
	public void userIsLoggedIntoTheParaBankApplication() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		homePage = loginPage.loginToApplication(ConfigManager.getInstance().getUsername(),
				ConfigManager.getInstance().getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after login.");
	}

	@When("user clicks on Accounts Overview link")
	public void userClicksOnAccountsOverviewLink() {
		accountsOverviewPage = homePage.clickAccountsOverview();
	}

	@Then("user should be navigated to the Accounts Overview page")
	public void userShouldBeNavigatedToTheAccountsOverviewPage() {
		UiAssertions.assertTrue(accountsOverviewPage.isAccountsOverviewPageDisplayed(),
				"Accounts Overview page is not displayed.");
		UiAssertions.assertEquals(accountsOverviewPage.getAccountsOverviewPageTitleText(), "Accounts Overview",
				"Accounts Overview page title text is incorrect.");
	}

	@Then("accounts table should be visible")
	public void accountsTableShouldBeVisible() {
		UiAssertions.assertTrue(accountsOverviewPage.isAccountsTableDisplayed(), "Accounts table is not displayed.");
	}

	@Then("at least one account should be displayed")
	public void atLeastOneAccountShouldBeDisplayed() {
		UiAssertions.assertTrue(accountsOverviewPage.isAtLeastOneAccountDisplayed(),
				"No account row is displayed in the accounts table.");
	}
}