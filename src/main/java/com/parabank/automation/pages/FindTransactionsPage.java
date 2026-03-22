package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class FindTransactionsPage extends BasePage {

	private final By pageTitle = By.xpath("//h1[contains(@class,'title') and normalize-space()='Find Transactions']");
	private final By accountDropdown = By.id("accountId");
	private final By amountField = By.id("amount");
	private final By findByAmountButton = By.id("findByAmount");
	private final By transactionsTable = By.id("transactionTable");

	public String getFindTransactionsPageTitleText() {
		WebElement titleElement = waitUtils.waitForElement(pageTitle, WaitStrategy.VISIBLE);
		return titleElement.getText().trim();
	}

	public FindTransactionsPage selectAccount(String accountNumber) {
		WebElement accountElement = waitUtils.waitForElement(accountDropdown, WaitStrategy.VISIBLE);
		Select select = new Select(accountElement);
		select.selectByVisibleText(accountNumber);
		return this;
	}

	public FindTransactionsPage enterAmount(String amount) {
		WebElement amountElement = waitUtils.waitForElement(amountField, WaitStrategy.VISIBLE);
		amountElement.clear();
		amountElement.sendKeys(amount);
		return this;
	}

	public FindTransactionsPage clickFindByAmountButton() {
		elementUtils.click(findByAmountButton, WaitStrategy.CLICKABLE);
		return this;
	}

	public FindTransactionsPage findTransactionsByAmount(String accountNumber, String amount) {
		return selectAccount(accountNumber).enterAmount(amount).clickFindByAmountButton();
	}

	public boolean isTransactionsTableDisplayed() {
		try {
			waitUtils.waitForElement(transactionsTable, WaitStrategy.VISIBLE);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public boolean isAtLeastOneTransactionDisplayed() {
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='transactionTable']//tbody/tr"));
		return !rows.isEmpty();
	}

	public boolean isMatchingAmountDisplayed(String amount) {
		String expectedAmount = "$" + amount + ".00";
		List<WebElement> matchingCells = driver.findElements(
				By.xpath("//table[@id='transactionTable']//td[normalize-space()='" + expectedAmount + "']"));
		return !matchingCells.isEmpty();
	}
}