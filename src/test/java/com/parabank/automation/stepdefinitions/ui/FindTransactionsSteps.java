package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.dataproviders.FindTransactionsTestDataProvider;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.models.FindTransactionsTestData;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.pages.FindTransactionsPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import com.parabank.automation.pages.TransferFundsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FindTransactionsSteps {

	private LoginPage loginPage;
	private HomePage homePage;
	private TransferFundsPage transferFundsPage;
	private FindTransactionsPage findTransactionsPage;
	private FindTransactionsTestData findTransactionsTestData;
	private String transferredFromAccount;

	@Given("user is logged in and is on the home page for transaction search")
	public void userIsLoggedInAndIsOnTheHomePageForTransactionSearch() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey("validUser");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after login.");
		UiAssertions.assertTrue(homePage.isTransferFundsLinkDisplayed(), "Transfer Funds link is not displayed.");
		UiAssertions.assertTrue(homePage.isFindTransactionsLinkDisplayed(), "Find Transactions link is not displayed.");
	}

	@When("user completes a transfer using find transactions test data key {string}")
	public void userCompletesATransferUsingFindTransactionsTestDataKey(String testDataKey) {
		findTransactionsTestData = FindTransactionsTestDataProvider.getFindTransactionsTestDataByKey(testDataKey);

		transferFundsPage = homePage.clickTransferFunds();

		UiAssertions.assertTrue(transferFundsPage.isTransferFundsPageDisplayed(),
				"Transfer Funds page is not displayed.");

		transferFundsPage.enterAmount(findTransactionsTestData.getAmount()).selectFirstAvailableAccounts();

		transferredFromAccount = transferFundsPage.getSelectedFromAccount();

		transferFundsPage.clickTransferButton();

		UiAssertions.assertTrue(transferFundsPage.isTransferSuccessful(),
				"Transfer was not completed successfully before transaction search.");
	}

	@When("user navigates to Find Transactions page")
	public void userNavigatesToFindTransactionsPage() {
		homePage = new HomePage();
		findTransactionsPage = homePage.clickFindTransactions();
	}

	@Then("user should be navigated to the Find Transactions page")
	public void userShouldBeNavigatedToTheFindTransactionsPage() {
		UiAssertions.assertEquals(findTransactionsPage.getFindTransactionsPageTitleText(), "Find Transactions",
				"Find Transactions page title is incorrect.");
	}

	@When("user searches transactions by amount using the same test data key {string}")
	public void userSearchesTransactionsByAmountUsingTheSameTestDataKey(String testDataKey) {
		findTransactionsTestData = FindTransactionsTestDataProvider.getFindTransactionsTestDataByKey(testDataKey);

		findTransactionsPage = findTransactionsPage.findTransactionsByAmount(transferredFromAccount,
				findTransactionsTestData.getAmount());
	}

	@Then("matching transactions should be displayed")
	public void matchingTransactionsShouldBeDisplayed() {
		UiAssertions.assertTrue(findTransactionsPage.isTransactionsTableDisplayed(),
				"Transactions table is not displayed.");
		UiAssertions.assertTrue(findTransactionsPage.isAtLeastOneTransactionDisplayed(),
				"No transaction rows are displayed.");
	}

	@Then("displayed transaction amount should match the searched amount")
	public void displayedTransactionAmountShouldMatchTheSearchedAmount() {
		UiAssertions.assertTrue(findTransactionsPage.isMatchingAmountDisplayed(findTransactionsTestData.getAmount()),
				"No transaction with the searched amount is displayed.");
	}
}