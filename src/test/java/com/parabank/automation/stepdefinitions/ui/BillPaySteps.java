package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.assertions.UiAssertions;
import com.parabank.automation.dataproviders.BillPayTestDataProvider;
import com.parabank.automation.dataproviders.LoginTestDataProvider;
import com.parabank.automation.models.BillPayTestData;
import com.parabank.automation.models.LoginTestData;
import com.parabank.automation.pages.BillPayPage;
import com.parabank.automation.pages.HomePage;
import com.parabank.automation.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BillPaySteps {

	private LoginPage loginPage;
	private HomePage homePage;
	private BillPayPage billPayPage;
	private BillPayTestData billPayTestData;

	private String submittedPayeeName;
	private String selectedFromAccount;

	@Given("user is logged in and is on the home page for bill payment")
	public void userIsLoggedInAndIsOnTheHomePageForBillPayment() {
		loginPage = new LoginPage();
		UiAssertions.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed.");

		LoginTestData loginTestData = LoginTestDataProvider.getLoginTestDataByKey("validUser");

		homePage = loginPage.loginToApplication(loginTestData.getUsername(), loginTestData.getPassword());

		UiAssertions.assertTrue(homePage.isHomePageDisplayed(), "Home page is not displayed after login.");
		UiAssertions.assertTrue(homePage.isBillPayLinkDisplayed(), "Bill Pay link is not displayed.");
	}

	@When("user navigates to Bill Pay page")
	public void userNavigatesToBillPayPage() {
		billPayPage = homePage.clickBillPay();
	}

	@Then("user should be navigated to the Bill Pay page")
	public void userShouldBeNavigatedToTheBillPayPage() {
		UiAssertions.assertEquals(billPayPage.getBillPayPageTitleText(), "Bill Payment Service",
				"Bill Pay page title is incorrect.");
	}

	@When("user submits the bill payment using test data key {string}")
	public void userSubmitsTheBillPaymentUsingTestDataKey(String testDataKey) {
		billPayTestData = BillPayTestDataProvider.getBillPayTestDataByKey(testDataKey);

		String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(8);
		submittedPayeeName = billPayTestData.getPayeeName() + "_" + uniqueSuffix;

		selectedFromAccount = billPayPage.getFirstValidFromAccount();

		billPayPage = billPayPage.submitBillPayment(submittedPayeeName, billPayTestData.getAddress(),
				billPayTestData.getCity(), billPayTestData.getState(), billPayTestData.getZipCode(),
				billPayTestData.getPhoneNumber(), billPayTestData.getAccountNumber(), billPayTestData.getAmount());
	}

	@Then("bill payment should be completed successfully")
	public void billPaymentShouldBeCompletedSuccessfully() {
		UiAssertions.assertEquals(billPayPage.getBillPaymentCompleteTitleText(), "Bill Payment Complete",
				"Bill payment success title is incorrect.");

		String expectedMessage = "Bill Payment to " + submittedPayeeName + " in the amount of $"
				+ billPayTestData.getAmount() + ".00" + " from account " + selectedFromAccount + " was successful.";

		UiAssertions.assertEquals(billPayPage.getBillPaymentResultMessage(), expectedMessage,
				"Bill payment success message is incorrect.");
	}
}