package com.parabank.automation.listeners;

import com.parabank.automation.reports.ExtentManager;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class SuiteListener implements ISuiteListener {

	@Override
	public void onStart(ISuite suite) {
		ExtentManager.initialize();
	}

	@Override
	public void onFinish(ISuite suite) {
		ExtentManager.flushReport();
	}
}