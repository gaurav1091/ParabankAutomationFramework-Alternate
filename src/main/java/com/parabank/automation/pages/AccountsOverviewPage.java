package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountsOverviewPage extends BasePage {

	private static final By ACCOUNTS_OVERVIEW_TITLE = By.cssSelector("div#showOverview h1.title");
	private static final By ACCOUNTS_TABLE = By.id("accountTable");
	private static final By ACCOUNT_NUMBER_LINKS = By.cssSelector("#accountTable tbody tr td:first-child a");

	public boolean isAccountsOverviewPageDisplayed() {
		waitForPageToBeStable();
		return elementUtils.isDisplayed(ACCOUNTS_OVERVIEW_TITLE, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(ACCOUNTS_TABLE, WaitStrategy.VISIBLE);
	}

	public String getAccountsOverviewPageTitleText() {
		return elementUtils.getText(ACCOUNTS_OVERVIEW_TITLE, WaitStrategy.VISIBLE);
	}

	public boolean isAccountsTableDisplayed() {
		return elementUtils.isDisplayed(ACCOUNTS_TABLE, WaitStrategy.VISIBLE);
	}

	public boolean isAtLeastOneAccountDisplayed() {
		return !getDisplayedAccountNumbers().isEmpty();
	}

	public List<String> getDisplayedAccountNumbers() {
		List<String> accountNumbers = new ArrayList<>();
		List<WebElement> accountLinks = driver.findElements(ACCOUNT_NUMBER_LINKS);

		for (WebElement accountLink : accountLinks) {
			String accountNumber = accountLink.getText().trim();
			if (!accountNumber.isEmpty()) {
				accountNumbers.add(accountNumber);
			}
		}

		return accountNumbers;
	}

	public int extractCustomerIdFromPageSource() {
		String pageSource = driver.getPageSource();
		Pattern pattern = Pattern.compile("customers/.*?(\\d+).*?/accounts", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(pageSource);

		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}

		throw new RuntimeException("Unable to extract customer id from Accounts Overview page source.");
	}
}