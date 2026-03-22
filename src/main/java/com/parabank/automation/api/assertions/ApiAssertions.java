package com.parabank.automation.api.assertions;

import com.parabank.automation.utils.JsonSchemaValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ApiAssertions {

	private ApiAssertions() {
	}

	public static void assertStatusCode(int actualStatusCode, int expectedStatusCode, String message) {
		if (actualStatusCode != expectedStatusCode) {
			throw new AssertionError(
					message + " | Expected=[" + expectedStatusCode + "] | Actual=[" + actualStatusCode + "]");
		}
	}

	public static void assertListNotEmpty(List<?> values, String message) {
		if (values == null || values.isEmpty()) {
			throw new AssertionError(message);
		}
	}

	public static void assertCollectionsMatchIgnoringOrder(List<String> actual, List<String> expected, String message) {
		Set<String> actualSet = new HashSet<>(actual);
		Set<String> expectedSet = new HashSet<>(expected);

		if (!actualSet.equals(expectedSet)) {
			throw new AssertionError(message + " | Expected=" + expectedSet + " | Actual=" + actualSet);
		}
	}

	public static void assertJsonMatchesSchema(Object payload, String schemaClasspathResource, String message) {
		List<String> violations = JsonSchemaValidator.validate(payload, schemaClasspathResource);

		if (!violations.isEmpty()) {
			throw new AssertionError(
					message + " | Schema Resource=" + schemaClasspathResource + " | Violations=" + violations);
		}
	}

	public static void assertAllAccountsHaveValidCoreFields(List<Map<String, Object>> accounts, String message) {
		if (accounts == null || accounts.isEmpty()) {
			throw new AssertionError(message + " | Account list is empty.");
		}

		for (Map<String, Object> account : accounts) {
			if (account == null) {
				throw new AssertionError(message + " | Encountered null account object.");
			}

			Object id = account.get("id");
			Object balance = account.get("balance");

			if (id == null || String.valueOf(id).trim().isEmpty()) {
				throw new AssertionError(message + " | Invalid account id found: " + id);
			}

			if (balance == null) {
				throw new AssertionError(message + " | Account balance is null for account id: " + id);
			}
		}
	}
}