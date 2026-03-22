package com.parabank.automation.base;

import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.utils.ElementUtils;
import com.parabank.automation.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

	protected WebDriver driver;
	protected WaitUtils waitUtils;
	protected ElementUtils elementUtils;

	protected BasePage() {
		this.driver = DriverManager.getDriver();
		this.waitUtils = new WaitUtils();
		this.elementUtils = new ElementUtils();
		PageFactory.initElements(driver, this);
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public void navigateTo(String url) {
		driver.get(url);
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void waitForPageToBeStable() {
		waitUtils.waitForPageToBeStable();
	}
}