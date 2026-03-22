package com.parabank.automation.runners;

import com.parabank.automation.config.FrameworkConstants;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features/api", glue = { "com.parabank.automation.stepdefinitions.api",
		"com.parabank.automation.hooks" }, plugin = { "pretty", "summary",
				"json:" + FrameworkConstants.CUCUMBER_JSON_REPORT_FILE,
				"junit:" + FrameworkConstants.CUCUMBER_JUNIT_REPORT_FILE,
				"html:" + FrameworkConstants.CUCUMBER_HTML_REPORT_FILE }, tags = "@api and not @manual", monochrome = true, publish = false)
public class ApiTestRunner extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}
}