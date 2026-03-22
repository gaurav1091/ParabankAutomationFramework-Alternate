package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.OpenNewAccountTestData;

import java.util.List;

public final class OpenNewAccountTestDataProvider {

	private static final String OPEN_NEW_ACCOUNT_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "openNewAccount.json";

	private OpenNewAccountTestDataProvider() {
	}

	public static OpenNewAccountTestData getOpenNewAccountTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(OPEN_NEW_ACCOUNT_TEST_DATA_FILE_PATH,
				new TypeReference<List<OpenNewAccountTestData>>() {
				}, OpenNewAccountTestData::getKey, key);
	}
}