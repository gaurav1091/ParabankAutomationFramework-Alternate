package com.parabank.automation.context;

public final class ContextObjectManager {

	private static final ThreadLocal<TestContext> TEST_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

	private ContextObjectManager() {
	}

	public static void initializeContext() {
		if (TEST_CONTEXT_THREAD_LOCAL.get() == null) {
			TEST_CONTEXT_THREAD_LOCAL.set(new TestContext());
		}
	}

	public static TestContext getTestContext() {
		initializeContext();
		return TEST_CONTEXT_THREAD_LOCAL.get();
	}

	public static ScenarioContext getScenarioContext() {
		return getTestContext().getScenarioContext();
	}

	public static void unload() {
		TestContext testContext = TEST_CONTEXT_THREAD_LOCAL.get();
		if (testContext != null) {
			testContext.getScenarioContext().clear();
		}
		TEST_CONTEXT_THREAD_LOCAL.remove();
	}
}