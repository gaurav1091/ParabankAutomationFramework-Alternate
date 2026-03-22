package com.parabank.automation.listeners;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.reports.ExtentTestManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

	private static final String RETRY_COUNT_ATTRIBUTE = "retryCurrentCount";
	private static final String RETRY_MAX_COUNT_ATTRIBUTE = "retryMaxCount";

	@Override
	public boolean retry(ITestResult result) {
		ConfigManager configManager = ConfigManager.getInstance();

		if (!configManager.isRetryEnabled()) {
			return false;
		}

		int maxRetryCount = configManager.getRetryCount();
		int currentRetryCount = getCurrentRetryCount(result);

		result.setAttribute(RETRY_MAX_COUNT_ATTRIBUTE, maxRetryCount);

		if (currentRetryCount < maxRetryCount) {
			int nextRetryAttempt = currentRetryCount + 1;
			result.setAttribute(RETRY_COUNT_ATTRIBUTE, nextRetryAttempt);

			String methodName = result.getMethod() != null ? result.getMethod().getMethodName() : "Unknown Test Method";
			String message = "Retrying failed test. Attempt " + nextRetryAttempt + " of " + maxRetryCount
					+ " for method: " + methodName;

			ExtentTestManager.warning(message);
			return true;
		}

		return false;
	}

	private int getCurrentRetryCount(ITestResult result) {
		Object retryCountAttribute = result.getAttribute(RETRY_COUNT_ATTRIBUTE);

		if (retryCountAttribute instanceof Integer) {
			return (Integer) retryCountAttribute;
		}

		return 0;
	}
}