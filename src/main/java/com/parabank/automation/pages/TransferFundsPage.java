package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class TransferFundsPage extends BasePage {

	private static final By TRANSFER_FUNDS_TITLE = By.cssSelector("div#showForm h1.title");
	private static final By AMOUNT_TEXT_BOX = By.id("amount");
	private static final By FROM_ACCOUNT_DROPDOWN = By.id("fromAccountId");
	private static final By TO_ACCOUNT_DROPDOWN = By.id("toAccountId");
	private static final By TRANSFER_BUTTON = By.cssSelector("input[value='Transfer']");
	private static final By TRANSFER_COMPLETE_TITLE = By.cssSelector("div#showResult h1.title");
	private static final By TRANSFER_RESULT_MESSAGE = By.cssSelector("div#showResult p");
	private static final By TRANSFERRED_AMOUNT_VALUE = By.id("amountResult");

	public boolean isTransferFundsPageDisplayed() {
		waitUtils.waitForPageLoadComplete();
		return elementUtils.isDisplayed(TRANSFER_FUNDS_TITLE, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(AMOUNT_TEXT_BOX, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(FROM_ACCOUNT_DROPDOWN, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(TO_ACCOUNT_DROPDOWN, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(TRANSFER_BUTTON, WaitStrategy.CLICKABLE);
	}

	public String getTransferFundsPageTitleText() {
		return elementUtils.getText(TRANSFER_FUNDS_TITLE, WaitStrategy.VISIBLE);
	}

	public TransferFundsPage enterAmount(String amount) {
		elementUtils.type(AMOUNT_TEXT_BOX, amount, WaitStrategy.VISIBLE);
		return this;
	}

	public TransferFundsPage selectFirstAvailableAccounts() {
		WebElement fromDropdown = waitUtils.waitForElement(FROM_ACCOUNT_DROPDOWN, WaitStrategy.VISIBLE);
		WebElement toDropdown = waitUtils.waitForElement(TO_ACCOUNT_DROPDOWN, WaitStrategy.VISIBLE);

		Select fromSelect = new Select(fromDropdown);
		Select toSelect = new Select(toDropdown);

		List<WebElement> fromOptions = fromSelect.getOptions();
		List<WebElement> toOptions = toSelect.getOptions();

		if (fromOptions == null || fromOptions.isEmpty() || toOptions == null || toOptions.isEmpty()) {
			throw new RuntimeException("No accounts available in transfer dropdowns.");
		}

		String fromValueToUse = null;

		for (WebElement option : fromOptions) {
			String optionText = option.getText().trim();
			String optionValue = option.getAttribute("value");

			if (!optionText.isEmpty() && optionValue != null && !optionValue.trim().isEmpty()) {
				fromSelect.selectByValue(optionValue);
				fromValueToUse = optionValue.trim();
				break;
			}
		}

		if (fromValueToUse == null) {
			throw new RuntimeException("No valid source account available in transfer dropdown.");
		}

		boolean targetSelected = false;

		for (WebElement option : toOptions) {
			String optionText = option.getText().trim();
			String optionValue = option.getAttribute("value");

			if (!optionText.isEmpty() && optionValue != null && !optionValue.trim().isEmpty()
					&& !optionValue.trim().equals(fromValueToUse)) {
				toSelect.selectByValue(optionValue);
				targetSelected = true;
				break;
			}
		}

		if (!targetSelected) {
			for (WebElement option : toOptions) {
				String optionText = option.getText().trim();
				String optionValue = option.getAttribute("value");

				if (!optionText.isEmpty() && optionValue != null && !optionValue.trim().isEmpty()) {
					toSelect.selectByValue(optionValue);
					targetSelected = true;
					break;
				}
			}
		}

		if (!targetSelected) {
			throw new RuntimeException("No valid destination account available in transfer dropdown.");
		}

		return this;
	}

	public String getSelectedFromAccount() {
		WebElement fromDropdown = waitUtils.waitForElement(FROM_ACCOUNT_DROPDOWN, WaitStrategy.VISIBLE);
		Select fromSelect = new Select(fromDropdown);
		return fromSelect.getFirstSelectedOption().getText().trim();
	}

	public TransferFundsPage clickTransferButton() {
		elementUtils.click(TRANSFER_BUTTON, WaitStrategy.CLICKABLE);
		waitForTransferResult();
		return this;
	}

	public TransferFundsPage transferFunds(String amount) {
		return enterAmount(amount).selectFirstAvailableAccounts().clickTransferButton();
	}

	public boolean isTransferSuccessful() {
		waitForTransferResult();
		return elementUtils.isDisplayed(TRANSFER_COMPLETE_TITLE, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(TRANSFER_RESULT_MESSAGE, WaitStrategy.VISIBLE);
	}

	public String getTransferCompleteTitleText() {
		waitForTransferResult();
		return elementUtils.getText(TRANSFER_COMPLETE_TITLE, WaitStrategy.VISIBLE);
	}

	public String getTransferResultMessage() {
		waitForTransferResult();
		return elementUtils.getText(TRANSFER_RESULT_MESSAGE, WaitStrategy.VISIBLE);
	}

	public String getTransferredAmountValue() {
		waitForTransferResult();
		return elementUtils.getText(TRANSFERRED_AMOUNT_VALUE, WaitStrategy.VISIBLE);
	}

	private void waitForTransferResult() {
		waitUtils.waitForPageLoadComplete();
		waitUtils.waitForVisibilityOf(TRANSFER_COMPLETE_TITLE);
		waitUtils.waitForVisibilityOf(TRANSFER_RESULT_MESSAGE);
	}
}