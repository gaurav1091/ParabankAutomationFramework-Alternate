package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.TransferFundsTestData;

import java.util.List;

public final class TransferFundsTestDataProvider {

	private static final String TRANSFER_FUNDS_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "transferFunds.json";

	private TransferFundsTestDataProvider() {
	}

	public static TransferFundsTestData getTransferFundsTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(TRANSFER_FUNDS_TEST_DATA_FILE_PATH,
				new TypeReference<List<TransferFundsTestData>>() {
				}, TransferFundsTestData::getKey, key);
	}
}