package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.dataproviders.TransferFundsTestDataProvider;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.models.TransferFundsTestData;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.pages.TransferFundsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TransferFundsSteps {

	private LoginPage loginPage;
	private HomePage homePage;
	private TransferFundsPage transferFundsPage;
	private TransferFundsTestData transferFundsTestData;

	@Given("user is logged in and is on the home page")
	public void userIsLoggedInAndIsOnTheHomePage() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey("validUser");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after login.");
		UiAssertions.assertTrue(homePage.isTransferFundsLinkDisplayed(), "Transfer Funds link is not displayed.");
	}

	@When("user navigates to Transfer Funds page")
	public void userNavigatesToTransferFundsPage() {
		transferFundsPage = homePage.clickTransferFunds();
	}

	@Then("user should be navigated to the Transfer Funds page")
	public void userShouldBeNavigatedToTheTransferFundsPage() {
		UiAssertions.assertTrue(transferFundsPage.isTransferFundsPageDisplayed(),
				"Transfer Funds page is not displayed.");
		UiAssertions.assertEquals(transferFundsPage.getTransferFundsPageTitleText(), "Transfer Funds",
				"Transfer Funds page title is incorrect.");
	}

	@When("user transfers funds using test data key {string}")
	public void userTransfersFundsUsingTestDataKey(String testDataKey) {
		transferFundsTestData = TransferFundsTestDataProvider.getTransferFundsTestDataByKey(testDataKey);

		transferFundsPage = transferFundsPage.transferFunds(transferFundsTestData.getAmount());
	}

	@Then("transfer should be completed successfully")
	public void transferShouldBeCompletedSuccessfully() {
		UiAssertions.assertTrue(transferFundsPage.isTransferSuccessful(), "Transfer was not completed successfully.");
		UiAssertions.assertEquals(transferFundsPage.getTransferCompleteTitleText(), "Transfer Complete!",
				"Transfer completion title is incorrect.");
		UiAssertions.assertContains(transferFundsPage.getTransferResultMessage(), "has been transferred",
				"Transfer success message is incorrect.");
	}

	@Then("transferred amount should be displayed correctly")
	public void transferredAmountShouldBeDisplayedCorrectly() {
		UiAssertions.assertEquals(transferFundsPage.getTransferredAmountValue(),
				"$" + transferFundsTestData.getAmount() + ".00", "Transferred amount is incorrect.");
	}
}