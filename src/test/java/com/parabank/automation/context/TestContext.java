package com.parabank.automation.context;

public class TestContext {

	private final ScenarioContext scenarioContext;

	public TestContext() {
		this.scenarioContext = new ScenarioContext();
	}

	public ScenarioContext getScenarioContext() {
		return scenarioContext;
	}
}