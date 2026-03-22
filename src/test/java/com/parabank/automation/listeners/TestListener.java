package com.parabank.automation.listeners;

import com.aventstack.extentreports.Status;
import com.parabank.automation.reports.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

	private static final String RETRY_COUNT_ATTRIBUTE = "retryCurrentCount";
	private static final String RETRY_MAX_COUNT_ATTRIBUTE = "retryMaxCount";

	@Override
	public void onStart(ITestContext context) {
		// Reporting lifecycle is handled by SuiteListener and Cucumber hooks.
	}

	@Override
	public void onFinish(ITestContext context) {
		// Reporting lifecycle is handled by SuiteListener and Cucumber hooks.
	}

	@Override
	public void onTestStart(ITestResult result) {
		// Test node creation is handled in Cucumber @Before hook for correct scenario
		// naming.
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		if (ExtentTestManager.hasTest()) {
			int currentRetryCount = getIntegerAttribute(result, RETRY_COUNT_ATTRIBUTE);
			if (currentRetryCount > 0) {
				ExtentTestManager.log(Status.PASS, "Test passed after retry attempt " + currentRetryCount + ".");
			} else {
				ExtentTestManager.log(Status.INFO, "TestNG marked scenario execution as successful.");
			}
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		if (ExtentTestManager.hasTest()) {
			int currentRetryCount = getIntegerAttribute(result, RETRY_COUNT_ATTRIBUTE);
			int maxRetryCount = getIntegerAttribute(result, RETRY_MAX_COUNT_ATTRIBUTE);

			if (currentRetryCount > 0 && currentRetryCount <= maxRetryCount) {
				ExtentTestManager.log(Status.WARNING, "Test failed and is eligible for retry. Current retry attempt: "
						+ currentRetryCount + " of " + maxRetryCount + ".");
			}

			ExtentTestManager.log(Status.FAIL,
					"Failure Details -> Test Class: " + result.getTestClass().getName() + " | Method: "
							+ result.getMethod().getMethodName() + " | Duration: "
							+ Math.max(0L, result.getEndMillis() - result.getStartMillis()) + " ms");

			Throwable throwable = result.getThrowable();

			if (throwable != null) {
				ExtentTestManager.log(Status.FAIL,
						"Failure Message -> " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
				ExtentTestManager.log(Status.FAIL, throwable);
			} else {
				ExtentTestManager.log(Status.FAIL, "TestNG marked scenario execution as failed.");
			}
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		if (ExtentTestManager.hasTest()) {
			Throwable throwable = result.getThrowable();

			if (throwable != null) {
				ExtentTestManager.log(Status.SKIP, throwable);
			} else {
				ExtentTestManager.log(Status.SKIP, "TestNG marked scenario execution as skipped.");
			}
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// Not used.
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		onTestFailure(result);
	}

	private int getIntegerAttribute(ITestResult result, String attributeName) {
		Object attributeValue = result.getAttribute(attributeName);

		if (attributeValue instanceof Integer) {
			return (Integer) attributeValue;
		}

		return 0;
	}
}