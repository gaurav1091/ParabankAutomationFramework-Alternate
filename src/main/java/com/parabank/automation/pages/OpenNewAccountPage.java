package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class OpenNewAccountPage extends BasePage {

	@FindBy(css = "div#openAccountForm h1.title")
	private WebElement openNewAccountTitle;

	@FindBy(id = "type")
	private WebElement accountTypeDropdown;

	@FindBy(id = "fromAccountId")
	private WebElement fromAccountDropdown;

	@FindBy(css = "button.button, input.button")
	private WebElement openNewAccountButton;

	@FindBy(css = "div#openAccountResult h1.title")
	private WebElement accountOpenedTitle;

	@FindBy(id = "newAccountId")
	private WebElement newAccountNumberLink;

	public boolean isOpenNewAccountPageDisplayed() {
		return elementUtils.isDisplayed(openNewAccountTitle, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(accountTypeDropdown, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(fromAccountDropdown, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(openNewAccountButton, WaitStrategy.CLICKABLE);
	}

	public String getOpenNewAccountPageTitleText() {
		return elementUtils.getText(openNewAccountTitle, WaitStrategy.VISIBLE);
	}

	public OpenNewAccountPage selectAccountType(String accountType) {
		elementUtils.selectByVisibleText(accountTypeDropdown, accountType);
		return this;
	}

	public OpenNewAccountPage selectFirstAvailableFromAccount() {
		Select select = new Select(fromAccountDropdown);
		List<WebElement> options = select.getOptions();

		if (options.isEmpty()) {
			throw new RuntimeException("No source account is available in the Open New Account dropdown.");
		}

		select.selectByIndex(0);
		return this;
	}

	public OpenNewAccountPage clickOpenNewAccountButton() {
		elementUtils.click(openNewAccountButton, WaitStrategy.CLICKABLE);
		return this;
	}

	public OpenNewAccountPage openNewAccount(String accountType) {
		return selectAccountType(accountType).selectFirstAvailableFromAccount().clickOpenNewAccountButton();
	}

	public boolean isAccountCreationSuccessful() {
		return elementUtils.isDisplayed(accountOpenedTitle, WaitStrategy.VISIBLE)
				&& elementUtils.isDisplayed(newAccountNumberLink, WaitStrategy.VISIBLE);
	}

	public String getAccountOpenedTitleText() {
		return elementUtils.getText(accountOpenedTitle, WaitStrategy.VISIBLE);
	}

	public String getNewAccountNumber() {
		return elementUtils.getText(newAccountNumberLink, WaitStrategy.VISIBLE);
	}

	public boolean isNewAccountNumberDisplayed() {
		return elementUtils.isDisplayed(newAccountNumberLink, WaitStrategy.VISIBLE);
	}
}