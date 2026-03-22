package com.parabank.automation.pages;

import com.parabank.automation.base.BasePage;
import com.parabank.automation.enums.WaitStrategy;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

	private static final By USERNAME_TEXT_BOX = By.name("username");
	private static final By USERNAME_TEXT_BOX_FALLBACK = By.cssSelector("input[name='username']");

	private static final By PASSWORD_TEXT_BOX = By.name("password");
	private static final By PASSWORD_TEXT_BOX_FALLBACK = By.cssSelector("input[name='password']");

	private static final By LOGIN_BUTTON = By.cssSelector("input[value='Log In']");
	private static final By LOGIN_BUTTON_FALLBACK = By.xpath("//input[@value='Log In']");

	private static final By LOGIN_ERROR_MESSAGE = By.cssSelector("div#rightPanel p.error");
	private static final By LOGIN_ERROR_MESSAGE_FALLBACK = By
			.xpath("//div[@id='rightPanel']//p[contains(@class,'error')]");

	public LoginPage enterUsername(String username) {
		elementUtils.type(USERNAME_TEXT_BOX, username, WaitStrategy.VISIBLE, USERNAME_TEXT_BOX_FALLBACK);
		return this;
	}

	public LoginPage enterPassword(String password) {
		elementUtils.type(PASSWORD_TEXT_BOX, password, WaitStrategy.VISIBLE, PASSWORD_TEXT_BOX_FALLBACK);
		return this;
	}

	public HomePage clickLoginButton() {
		elementUtils.click(LOGIN_BUTTON, WaitStrategy.CLICKABLE, LOGIN_BUTTON_FALLBACK);
		HomePage homePage = new HomePage();
		homePage.waitUntilLoaded();
		return homePage;
	}

	public LoginPage clickLoginButtonExpectingFailure() {
		elementUtils.click(LOGIN_BUTTON, WaitStrategy.CLICKABLE, LOGIN_BUTTON_FALLBACK);
		LoginPage refreshedLoginPage = new LoginPage();
		refreshedLoginPage.waitForFailedLoginResult();
		return refreshedLoginPage;
	}

	public HomePage loginToApplication(String username, String password) {
		return enterUsername(username).enterPassword(password).clickLoginButton();
	}

	public LoginPage loginToApplicationExpectingFailure(String username, String password) {
		return enterUsername(username).enterPassword(password).clickLoginButtonExpectingFailure();
	}

	public boolean isLoginPageDisplayed() {
		waitForPageToBeStable();
		return elementUtils.isDisplayed(USERNAME_TEXT_BOX, WaitStrategy.VISIBLE, USERNAME_TEXT_BOX_FALLBACK)
				&& elementUtils.isDisplayed(PASSWORD_TEXT_BOX, WaitStrategy.VISIBLE, PASSWORD_TEXT_BOX_FALLBACK)
				&& elementUtils.isDisplayed(LOGIN_BUTTON, WaitStrategy.CLICKABLE, LOGIN_BUTTON_FALLBACK);
	}

	public String getLoginPageTitle() {
		return getPageTitle();
	}

	public String getErrorMessage() {
		try {
			waitForFailedLoginResult();
			if (elementUtils.isDisplayed(LOGIN_ERROR_MESSAGE, WaitStrategy.VISIBLE, LOGIN_ERROR_MESSAGE_FALLBACK)) {
				return elementUtils.getText(LOGIN_ERROR_MESSAGE, WaitStrategy.VISIBLE, LOGIN_ERROR_MESSAGE_FALLBACK);
			}
			return "";
		} catch (Exception exception) {
			return "";
		}
	}

	public boolean isErrorMessageDisplayed() {
		waitForFailedLoginResult();
		return elementUtils.isDisplayed(LOGIN_ERROR_MESSAGE, WaitStrategy.VISIBLE, LOGIN_ERROR_MESSAGE_FALLBACK);
	}

	public LoginPage waitForFailedLoginResult() {
		waitForPageToBeStable();

		boolean errorVisible = waitUtils.waitForAnyVisibleSafely(LOGIN_ERROR_MESSAGE, LOGIN_ERROR_MESSAGE_FALLBACK);
		if (!errorVisible) {
			waitUtils.waitForVisibilityOf(USERNAME_TEXT_BOX);
			waitUtils.waitForVisibilityOf(PASSWORD_TEXT_BOX);
		}
		return this;
	}
}