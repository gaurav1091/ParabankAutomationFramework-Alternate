package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.FindTransactionsTestData;

import java.util.List;

public final class FindTransactionsTestDataProvider {

	private static final String FIND_TRANSACTIONS_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "findTransactions.json";

	private FindTransactionsTestDataProvider() {
	}

	public static FindTransactionsTestData getFindTransactionsTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(FIND_TRANSACTIONS_TEST_DATA_FILE_PATH,
				new TypeReference<List<FindTransactionsTestData>>() {
				}, FindTransactionsTestData::getKey, key);
	}
}