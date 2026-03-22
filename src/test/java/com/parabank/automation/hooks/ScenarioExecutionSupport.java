package com.parabank.automation.hooks;

import io.cucumber.java.Scenario;

import java.util.Collection;

public final class ScenarioExecutionSupport {

	private static final String UI_TAG = "@ui";
	private static final String HYBRID_TAG = "@hybrid";
	private static final String API_AUTHENTICATED_TAG = "@api_authenticated";

	private ScenarioExecutionSupport() {
	}

	public static boolean requiresBrowser(Scenario scenario) {
		if (scenario == null) {
			return false;
		}

		Collection<String> tags = scenario.getSourceTagNames();
		if (tags == null || tags.isEmpty()) {
			return false;
		}

		return tags.contains(UI_TAG) || tags.contains(HYBRID_TAG) || tags.contains(API_AUTHENTICATED_TAG);
	}

	public static String getExecutionModeDescription(Scenario scenario) {
		if (requiresBrowser(scenario)) {
			return "Browser-backed scenario execution";
		}
		return "API-only scenario execution";
	}
}