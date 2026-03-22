package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.LoginTestData;

import java.util.List;

public final class LoginTestDataProvider {

	private static final String LOGIN_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "loginTestData.json";

	private LoginTestDataProvider() {
	}

	public static LoginTestData getLoginTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(LOGIN_TEST_DATA_FILE_PATH, new TypeReference<List<LoginTestData>>() {
		}, LoginTestData::getKey, key);
	}
}