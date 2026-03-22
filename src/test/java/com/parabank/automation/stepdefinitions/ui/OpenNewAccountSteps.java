package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.dataproviders.OpenNewAccountTestDataProvider;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.models.OpenNewAccountTestData;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.pages.OpenNewAccountPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OpenNewAccountSteps {

	private LoginPage loginPage;
	private HomePage homePage;
	private OpenNewAccountPage openNewAccountPage;
	private OpenNewAccountTestData openNewAccountTestData;

	@Given("user is logged in and is on the home page for account creation")
	public void userIsLoggedInAndIsOnTheHomePageForAccountCreation() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey("validUser");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after login.");
		UiAssertions.assertTrue(homePage.isOpenNewAccountLinkDisplayed(), "Open New Account link is not displayed.");
	}

	@When("user navigates to Open New Account page")
	public void userNavigatesToOpenNewAccountPage() {
		openNewAccountPage = homePage.clickOpenNewAccount();
	}

	@Then("user should be navigated to the Open New Account page")
	public void userShouldBeNavigatedToTheOpenNewAccountPage() {
		UiAssertions.assertTrue(openNewAccountPage.isOpenNewAccountPageDisplayed(),
				"Open New Account page is not displayed.");
		UiAssertions.assertEquals(openNewAccountPage.getOpenNewAccountPageTitleText(), "Open New Account",
				"Open New Account page title is incorrect.");
	}

	@When("user creates a new account using test data key {string}")
	public void userCreatesANewAccountUsingTestDataKey(String testDataKey) {
		openNewAccountTestData = OpenNewAccountTestDataProvider.getOpenNewAccountTestDataByKey(testDataKey);

		openNewAccountPage = openNewAccountPage.openNewAccount(openNewAccountTestData.getAccountType());
	}

	@Then("new account should be created successfully")
	public void newAccountShouldBeCreatedSuccessfully() {
		UiAssertions.assertTrue(openNewAccountPage.isAccountCreationSuccessful(),
				"New account was not created successfully.");
		UiAssertions.assertEquals(openNewAccountPage.getAccountOpenedTitleText(), "Account Opened!",
				"Account creation success title is incorrect.");
	}

	@Then("new account number should be displayed")
	public void newAccountNumberShouldBeDisplayed() {
		UiAssertions.assertTrue(openNewAccountPage.isNewAccountNumberDisplayed(),
				"New account number is not displayed.");
		UiAssertions.assertNotEmpty(openNewAccountPage.getNewAccountNumber(), "New account number is empty.");
	}
}