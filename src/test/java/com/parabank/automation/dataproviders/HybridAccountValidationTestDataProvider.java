package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.HybridAccountValidationTestData;

import java.util.List;

public final class HybridAccountValidationTestDataProvider {

	private static final String HYBRID_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "hybridAccountValidation.json";

	private HybridAccountValidationTestDataProvider() {
	}

	public static HybridAccountValidationTestData getHybridAccountValidationTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(HYBRID_TEST_DATA_FILE_PATH,
				new TypeReference<List<HybridAccountValidationTestData>>() {
				}, HybridAccountValidationTestData::getKey, key);
	}
}