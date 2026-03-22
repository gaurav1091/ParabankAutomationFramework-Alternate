package com.parabank.automation.hooks;

import com.parabank.automation.config.ConfigManager;
import com.parabank.automation.config.EnvironmentManager;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.driver.DriverFactory;
import com.parabank.automation.driver.DriverManager;
import com.parabank.automation.reports.ExtentManager;
import com.parabank.automation.reports.ExtentTestManager;
import com.parabank.automation.utils.BrowserStackUtils;
import com.parabank.automation.utils.FailureDiagnosticsUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

import java.util.Collection;
import java.util.stream.Collectors;

public class TestHooks {

	@Before(order = -1)
	public void initializeScenarioContext() {
		ContextObjectManager.initializeContext();
	}

	@Before(order = 0)
	public void startScenarioReport(Scenario scenario) {
		ExtentTestManager.setTest(ExtentManager.createTest(scenario.getName()));

		ExtentTestManager.info("Scenario execution started.");
		ExtentTestManager.info("Thread ID: " + Thread.currentThread().getId());
		ExtentTestManager.info("Browser: " + ConfigManager.getInstance().getBrowser());
		ExtentTestManager.info("Environment: " + EnvironmentManager.getCurrentEnvironment());
		ExtentTestManager.info("Base URL: " + ConfigManager.getInstance().getBaseUrl());
		ExtentTestManager.info("Execution Mode: " + ConfigManager.getInstance().getExecutionMode());
		ExtentTestManager.info("Remote Provider: " + ConfigManager.getInstance().getRemoteProvider());
		ExtentTestManager
				.info("Scenario Execution Type: " + ScenarioExecutionSupport.getExecutionModeDescription(scenario));
		ExtentTestManager.info("Browser Required For Scenario: " + ScenarioExecutionSupport.requiresBrowser(scenario));

		Collection<String> tags = scenario.getSourceTagNames();
		if (tags != null && !tags.isEmpty()) {
			String joinedTags = tags.stream().sorted().collect(Collectors.joining(", "));
			ExtentTestManager.info("Scenario Tags: " + joinedTags);
		} else {
			ExtentTestManager.info("Scenario Tags: None");
		}
	}

	@Before(order = 1)
	public void setUp(Scenario scenario) {
		if (!ScenarioExecutionSupport.requiresBrowser(scenario)) {
			ExtentTestManager.info("Skipping browser startup because this is an API-only scenario.");
			return;
		}

		WebDriver driver = DriverFactory.initializeDriver();

		if (ConfigManager.getInstance().isBrowserStackExecution()) {
			BrowserStackUtils.setSessionName(scenario.getName());
		}

		driver.get(ConfigManager.getInstance().getBaseUrl());
		ExtentTestManager.info("Application launched successfully.");
	}

	@After(order = 1)
	public void updateScenarioStatus(Scenario scenario) {
		String sanitizedScenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9_-]", "_");

		if (scenario.isFailed()) {
			ExtentTestManager.fail("Scenario failed: " + scenario.getName());

			if (DriverManager.hasDriver()) {
				BrowserStackUtils.setSessionStatus(false, "Scenario failed: " + scenario.getName());
				FailureDiagnosticsUtils.logUiFailureDetails(scenario.getName());
				ExtentTestManager.captureAndAttachFailureScreenshot("scenario_failure_" + sanitizedScenarioName);
			} else {
				ExtentTestManager.info(
						"Skipping UI failure diagnostics and screenshot because no browser was started for this scenario.");
			}
		} else {
			ExtentTestManager.pass("Scenario passed: " + scenario.getName());

			if (DriverManager.hasDriver()) {
				BrowserStackUtils.setSessionStatus(true, "Scenario passed: " + scenario.getName());
				ExtentTestManager.captureAndAttachPassScreenshot("scenario_pass_" + sanitizedScenarioName);
			}
		}
	}

	@After(order = 0)
	public void tearDown(Scenario scenario) {
		if (DriverManager.hasDriver()) {
			DriverFactory.quitDriver();
		} else {
			ExtentTestManager.info("No browser teardown required for this scenario.");
		}

		ExtentManager.flushReport();
		ExtentTestManager.unload();
	}

	@After(order = -1)
	public void clearScenarioContext() {
		ContextObjectManager.unload();
	}
}
