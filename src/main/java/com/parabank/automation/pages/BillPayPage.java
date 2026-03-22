package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BillPayPage extends BasePage {

	@FindBy(css = "div#billpayForm h1.title")
	private WebElement billPayTitle;

	@FindBy(name = "payee.name")
	private WebElement payeeNameTextBox;

	@FindBy(name = "payee.address.street")
	private WebElement addressTextBox;

	@FindBy(name = "payee.address.city")
	private WebElement cityTextBox;

	@FindBy(name = "payee.address.state")
	private WebElement stateTextBox;

	@FindBy(name = "payee.address.zipCode")
	private WebElement zipCodeTextBox;

	@FindBy(name = "payee.phoneNumber")
	private WebElement phoneNumberTextBox;

	@FindBy(name = "payee.accountNumber")
	private WebElement accountNumberTextBox;

	@FindBy(name = "verifyAccount")
	private WebElement verifyAccountTextBox;

	@FindBy(name = "amount")
	private WebElement amountTextBox;

	@FindBy(name = "fromAccountId")
	private WebElement fromAccountDropdown;

	@FindBy(css = "input[value='Send Payment']")
	private WebElement sendPaymentButton;

	@FindBy(css = "div#billpayResult h1.title")
	private WebElement billPaymentCompleteTitle;

	@FindBy(css = "div#billpayResult p")
	private WebElement billPaymentResultMessage;

	public boolean isBillPayPageDisplayed() {
		return elementUtils.isDisplayed(billPayTitle, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(payeeNameTextBox, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(amountTextBox, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(fromAccountDropdown, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(sendPaymentButton, WaitStrategy.CLICKABLE);
	}

	public String getBillPayPageTitleText() {
		return elementUtils.getText(billPayTitle, WaitStrategy.VISIBLE);
	}

	public BillPayPage enterPayeeName(String payeeName) {
		typeAndVerify(payeeNameTextBox, payeeName);
		return this;
	}

	public BillPayPage enterAddress(String address) {
		typeAndVerify(addressTextBox, address);
		return this;
	}

	public BillPayPage enterCity(String city) {
		typeAndVerify(cityTextBox, city);
		return this;
	}

	public BillPayPage enterState(String state) {
		typeAndVerify(stateTextBox, state);
		return this;
	}

	public BillPayPage enterZipCode(String zipCode) {
		typeAndVerify(zipCodeTextBox, zipCode);
		return this;
	}

	public BillPayPage enterPhoneNumber(String phoneNumber) {
		typeAndVerify(phoneNumberTextBox, phoneNumber);
		return this;
	}

	public BillPayPage enterAccountNumber(String accountNumber) {
		typeAndVerify(accountNumberTextBox, accountNumber);
		return this;
	}

	public BillPayPage enterVerifyAccountNumber(String verifyAccountNumber) {
		typeAndVerify(verifyAccountTextBox, verifyAccountNumber);
		return this;
	}

	public BillPayPage enterAmount(String amount) {
		typeAndVerify(amountTextBox, amount);
		return this;
	}

	public BillPayPage selectFirstValidFromAccount() {
		waitUtils.waitForElement(fromAccountDropdown, WaitStrategy.VISIBLE);

		Select select = new Select(fromAccountDropdown);
		List<WebElement> options = select.getOptions();

		if (options == null || options.isEmpty()) {
			throw new RuntimeException("No source account is available in Bill Pay dropdown.");
		}

		boolean accountSelected = false;

		for (int i = 0; i < options.size(); i++) {
			String optionText = options.get(i).getText().trim();
			String optionValue = options.get(i).getAttribute("value");

			if (!optionText.isEmpty() && optionValue != null && !optionValue.trim().isEmpty()
					&& !optionText.equalsIgnoreCase("Select Account")) {
				select.selectByIndex(i);
				accountSelected = true;
				break;
			}
		}

		if (!accountSelected) {
			throw new RuntimeException("No valid source account option was found in Bill Pay dropdown.");
		}

		return this;
	}

	public String getFirstValidFromAccount() {
		waitUtils.waitForElement(fromAccountDropdown, WaitStrategy.VISIBLE);

		Select select = new Select(fromAccountDropdown);
		List<WebElement> options = select.getOptions();

		if (options == null || options.isEmpty()) {
			throw new RuntimeException("No source account is available in Bill Pay dropdown.");
		}

		for (WebElement option : options) {
			String optionText = option.getText().trim();
			String optionValue = option.getAttribute("value");

			if (!optionText.isEmpty() && optionValue != null && !optionValue.trim().isEmpty()
					&& !optionText.equalsIgnoreCase("Select Account")) {
				return optionText;
			}
		}

		throw new RuntimeException("No valid source account option was found in Bill Pay dropdown.");
	}

	public String getSelectedFromAccount() {
		Select select = new Select(fromAccountDropdown);
		return select.getFirstSelectedOption().getText().trim();
	}

	public BillPayPage clickSendPaymentButton() {
		waitUtils.waitForElement(sendPaymentButton, WaitStrategy.CLICKABLE);
		elementUtils.click(sendPaymentButton, WaitStrategy.CLICKABLE);
		return this;
	}

	public BillPayPage submitBillPayment(String payeeName, String address, String city, String state, String zipCode,
			String phoneNumber, String accountNumber, String amount) {
		return enterPayeeName(payeeName).enterAddress(address).enterCity(city).enterState(state).enterZipCode(zipCode)
				.enterPhoneNumber(phoneNumber).enterAccountNumber(accountNumber).enterVerifyAccountNumber(accountNumber)
				.enterAmount(amount).selectFirstValidFromAccount().clickSendPaymentButton();
	}

	public boolean isBillPaymentSuccessful() {
		return elementUtils.isDisplayed(billPaymentCompleteTitle, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(billPaymentResultMessage, WaitStrategy.VISIBLE);
	}

	public String getBillPaymentCompleteTitleText() {
		return elementUtils.getText(billPaymentCompleteTitle, WaitStrategy.VISIBLE).trim();
	}

	public String getBillPaymentResultMessage() {
		return elementUtils.getText(billPaymentResultMessage, WaitStrategy.VISIBLE).trim();
	}

	private void typeAndVerify(WebElement element, String value) {
		elementUtils.type(element, value, WaitStrategy.VISIBLE);

		String actualValue = element.getAttribute("value");
		if (actualValue == null || !actualValue.equals(value)) {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) DriverManager.getDriver();
			jsExecutor.executeScript("arguments[0].value = arguments[1];", element, value);
		}
	}
}