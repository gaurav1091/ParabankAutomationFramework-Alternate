package com.parabank.automation.stepdefinitions.api;

import com.parabank.automation.api.assertions.ApiAssertions;
import com.parabank.automation.context.ContextObjectManager;
import com.parabank.automation.context.ScenarioContext;
import com.parabank.automation.enums.ContextKey;

import java.util.List;

public class CustomerApiSteps {

	private final ScenarioContext scenarioContext;

	public CustomerApiSteps() {
		this.scenarioContext = ContextObjectManager.getScenarioContext();
	}

	@io.cucumber.java.en.Then("API account ids should match the UI account ids from the authenticated session")
	public void apiAccountIdsShouldMatchTheUiAccountIdsFromTheAuthenticatedSession() {
		@SuppressWarnings("unchecked")
		List<String> uiAccountIds = (List<String>) scenarioContext.get(ContextKey.UI_ACCOUNT_IDS);

		@SuppressWarnings("unchecked")
		List<String> apiAccountIds = (List<String>) scenarioContext.get(ContextKey.API_ACCOUNT_IDS);

		ApiAssertions.assertListNotEmpty(uiAccountIds,
				"UI account ids were not captured during authenticated API setup.");
		ApiAssertions.assertListNotEmpty(apiAccountIds, "API account ids were not captured.");
		ApiAssertions.assertCollectionsMatchIgnoringOrder(apiAccountIds, uiAccountIds,
				"API account ids do not match UI account ids.");
	}
}