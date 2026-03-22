package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

	private LoginPage loginPage;
	private HomePage homePage;

	@Given("user is on the ParaBank login page")
	public void userIsOnTheParaBankLoginPage() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");
	}

	@When("user logs in with valid test data key {string}")
	public void userLogsInWithValidTestDataKey(String testDataKey) {
		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey(testDataKey);
		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());
	}

	@Then("user should be navigated to the home page")
	public void userShouldBeNavigatedToTheHomePage() {
		UiAssertions.assertTrue(homePage != null && homePage.isHomePageDisplayed(),
				"Home page is not displayed after login.");
	}

	@Then("logout link should be visible")
	public void logoutLinkShouldBeVisible() {
		UiAssertions.assertTrue(homePage != null && homePage.isLogoutLinkDisplayed(), "Logout link is not visible.");
	}

	@When("user attempts login failure with test data key {string}")
	public void userAttemptsLoginFailureWithTestDataKey(String testDataKey) {
		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey(testDataKey);
		loginPage = loginPage.loginToApplicationExpectingFailure(loginTestData.getUsername(),
				loginTestData.getPassword());
	}

	@Then("user should see login error message")
	public void userShouldSeeLoginErrorMessage() {
		boolean isDisplayed = loginPage != null && loginPage.isErrorMessageDisplayed();

		UiAssertions.assertTrue(isDisplayed, "Login error message is not displayed.");

		String actualErrorMessage = loginPage.getErrorMessage();
		UiAssertions.assertNotEmpty(actualErrorMessage, "Login error message is empty.");
	}
}