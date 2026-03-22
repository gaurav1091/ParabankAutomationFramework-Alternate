package com.parabank.automation.listeners;

import com.parabank.automation.config.ConfigManager;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.List;

public class ParallelSuiteListener implements IAlterSuiteListener {

	@Override
	public void alter(List<XmlSuite> suites) {
		if (suites == null || suites.isEmpty()) {
			return;
		}

		ConfigManager configManager = ConfigManager.getInstance();

		int threadCount = configManager.getThreadCount();
		int dataProviderThreadCount = configManager.getDataProviderThreadCount();
		XmlSuite.ParallelMode parallelMode = resolveParallelMode(configManager.getParallelMode());

		for (XmlSuite suite : suites) {
			suite.setParallel(parallelMode);
			suite.setThreadCount(threadCount);
			suite.setDataProviderThreadCount(dataProviderThreadCount);
			suite.setPreserveOrder(false);
			suite.setVerbose(1);

			List<XmlTest> tests = suite.getTests();
			if (tests != null) {
				for (XmlTest test : tests) {
					test.setPreserveOrder(false);
				}
			}
		}
	}

	private XmlSuite.ParallelMode resolveParallelMode(String parallelModeValue) {
		if (parallelModeValue == null) {
			return XmlSuite.ParallelMode.METHODS;
		}

		switch (parallelModeValue.trim().toLowerCase()) {
		case "methods":
			return XmlSuite.ParallelMode.METHODS;
		case "tests":
			return XmlSuite.ParallelMode.TESTS;
		case "classes":
			return XmlSuite.ParallelMode.CLASSES;
		case "instances":
			return XmlSuite.ParallelMode.INSTANCES;
		case "none":
			return XmlSuite.ParallelMode.NONE;
		default:
			return XmlSuite.ParallelMode.METHODS;
		}
	}
}