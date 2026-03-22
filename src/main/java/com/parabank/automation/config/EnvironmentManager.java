package com.parabank.automation.config;

import com.parabank.automation.enums.EnvironmentType;

public final class EnvironmentManager {

	private EnvironmentManager() {
	}

	public static String getCurrentEnvironment() {
		String environmentFromSystem = System.getProperty("env");

		if (environmentFromSystem != null && !environmentFromSystem.trim().isEmpty()) {
			return environmentFromSystem.trim().toLowerCase();
		}

		return "qa";
	}

	public static EnvironmentType getEnvironmentType() {
		String currentEnvironment = getCurrentEnvironment();

		switch (currentEnvironment.toLowerCase()) {
		case "qa":
			return EnvironmentType.QA;
		case "stage":
			return EnvironmentType.STAGE;
		case "dev":
			return EnvironmentType.DEV;
		default:
			throw new IllegalArgumentException("Unsupported environment: " + currentEnvironment);
		}
	}

	public static String getEnvironmentConfigFileName() {
		switch (getEnvironmentType()) {
		case QA:
			return FrameworkConstants.QA_CONFIG_FILE;
		case STAGE:
			return FrameworkConstants.STAGE_CONFIG_FILE;
		case DEV:
			return FrameworkConstants.DEV_CONFIG_FILE;
		default:
			throw new IllegalArgumentException("Unsupported environment: " + getCurrentEnvironment());
		}
	}
}