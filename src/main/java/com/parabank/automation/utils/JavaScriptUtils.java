package com.parabank.automation.utils;

import com.parabank.automation.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtils {

	private final WebDriver driver;
	private final JavascriptExecutor javascriptExecutor;

	public JavaScriptUtils() {
		this.driver = DriverManager.getDriver();
		this.javascriptExecutor = (JavascriptExecutor) driver;
	}

	public void clickUsingJavaScript(WebElement element) {
		javascriptExecutor.executeScript("arguments[0].click();", element);
	}

	public void scrollIntoView(WebElement element) {
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public String getPageInnerText() {
		return (String) javascriptExecutor.executeScript("return document.documentElement.innerText;");
	}

	public String getDocumentReadyState() {
		return (String) javascriptExecutor.executeScript("return document.readyState;");
	}
}