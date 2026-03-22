package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

	private static final By WELCOME_MESSAGE = By.cssSelector("p.smallText");

	private static final By LOGOUT_LINK = By.linkText("Log Out");
	private static final By LOGOUT_LINK_FALLBACK = By.xpath("//a[normalize-space()='Log Out']");

	private static final By ACCOUNTS_OVERVIEW_LINK = By.linkText("Accounts Overview");
	private static final By ACCOUNTS_OVERVIEW_LINK_FALLBACK = By.xpath("//a[normalize-space()='Accounts Overview']");

	private static final By TRANSFER_FUNDS_LINK = By.linkText("Transfer Funds");
	private static final By TRANSFER_FUNDS_LINK_FALLBACK = By.xpath("//a[normalize-space()='Transfer Funds']");

	private static final By OPEN_NEW_ACCOUNT_LINK = By.linkText("Open New Account");
	private static final By OPEN_NEW_ACCOUNT_LINK_FALLBACK = By.xpath("//a[normalize-space()='Open New Account']");

	private static final By BILL_PAY_LINK = By.linkText("Bill Pay");
	private static final By BILL_PAY_LINK_FALLBACK = By.xpath("//a[normalize-space()='Bill Pay']");

	private static final By FIND_TRANSACTIONS_LINK = By.linkText("Find Transactions");
	private static final By FIND_TRANSACTIONS_LINK_FALLBACK = By.xpath("//a[normalize-space()='Find Transactions']");

	public HomePage waitUntilLoaded() {
		waitForPageToBeStable();
		waitUtils.waitForAnyVisible(LOGOUT_LINK, LOGOUT_LINK_FALLBACK);
		waitUtils.waitForAnyVisible(ACCOUNTS_OVERVIEW_LINK, ACCOUNTS_OVERVIEW_LINK_FALLBACK);
		return this;
	}

	public boolean isHomePageDisplayed() {
		waitUntilLoaded();
		return elementUtils.isDisplayed(LOGOUT_LINK, WaitStrategy.VISIBLE, LOGOUT_LINK_FALLBACK) && elementUtils
				.isDisplayed(ACCOUNTS_OVERVIEW_LINK, WaitStrategy.VISIBLE, ACCOUNTS_OVERVIEW_LINK_FALLBACK);
	}

	public String getWelcomeMessage() {
		return elementUtils.getText(WELCOME_MESSAGE, WaitStrategy.VISIBLE);
	}

	public boolean isLogoutLinkDisplayed() {
		return elementUtils.isDisplayed(LOGOUT_LINK, WaitStrategy.VISIBLE, LOGOUT_LINK_FALLBACK);
	}

	public boolean isAccountsOverviewLinkDisplayed() {
		return elementUtils.isDisplayed(ACCOUNTS_OVERVIEW_LINK, WaitStrategy.VISIBLE, ACCOUNTS_OVERVIEW_LINK_FALLBACK);
	}

	public boolean isTransferFundsLinkDisplayed() {
		return elementUtils.isDisplayed(TRANSFER_FUNDS_LINK, WaitStrategy.VISIBLE, TRANSFER_FUNDS_LINK_FALLBACK);
	}

	public boolean isOpenNewAccountLinkDisplayed() {
		return elementUtils.isDisplayed(OPEN_NEW_ACCOUNT_LINK, WaitStrategy.VISIBLE, OPEN_NEW_ACCOUNT_LINK_FALLBACK);
	}

	public boolean isBillPayLinkDisplayed() {
		return elementUtils.isDisplayed(BILL_PAY_LINK, WaitStrategy.VISIBLE, BILL_PAY_LINK_FALLBACK);
	}

	public boolean isFindTransactionsLinkDisplayed() {
		return elementUtils.isDisplayed(FIND_TRANSACTIONS_LINK, WaitStrategy.VISIBLE, FIND_TRANSACTIONS_LINK_FALLBACK);
	}

	public AccountsOverviewPage clickAccountsOverview() {
		elementUtils.click(ACCOUNTS_OVERVIEW_LINK, WaitStrategy.CLICKABLE, ACCOUNTS_OVERVIEW_LINK_FALLBACK);
		return new AccountsOverviewPage();
	}

	public TransferFundsPage clickTransferFunds() {
		elementUtils.click(TRANSFER_FUNDS_LINK, WaitStrategy.CLICKABLE, TRANSFER_FUNDS_LINK_FALLBACK);
		return new TransferFundsPage();
	}

	public OpenNewAccountPage clickOpenNewAccount() {
		elementUtils.click(OPEN_NEW_ACCOUNT_LINK, WaitStrategy.CLICKABLE, OPEN_NEW_ACCOUNT_LINK_FALLBACK);
		return new OpenNewAccountPage();
	}

	public BillPayPage clickBillPay() {
		elementUtils.click(BILL_PAY_LINK, WaitStrategy.CLICKABLE, BILL_PAY_LINK_FALLBACK);
		return new BillPayPage();
	}

	public FindTransactionsPage clickFindTransactions() {
		elementUtils.click(FIND_TRANSACTIONS_LINK, WaitStrategy.CLICKABLE, FIND_TRANSACTIONS_LINK_FALLBACK);
		return new FindTransactionsPage();
	}

	public LoginPage clickLogout() {
		elementUtils.click(LOGOUT_LINK, WaitStrategy.CLICKABLE, LOGOUT_LINK_FALLBACK);
		return new LoginPage();
	}
}