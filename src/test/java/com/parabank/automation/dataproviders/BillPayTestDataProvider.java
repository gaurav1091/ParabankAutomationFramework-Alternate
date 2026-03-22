package com.parabank.automation.dataproviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.parabank.automation.config.FrameworkConstants;
import com.parabank.automation.models.BillPayTestData;

import java.util.List;

public final class BillPayTestDataProvider {

	private static final String BILL_PAY_TEST_DATA_FILE_PATH = FrameworkConstants.TEST_DATA_CLASSPATH_ROOT
			+ "billpay.json";

	private BillPayTestDataProvider() {
	}

	public static BillPayTestData getBillPayTestDataByKey(String key) {
		return TestDataProvider.getTestDataByKey(BILL_PAY_TEST_DATA_FILE_PATH,
				new TypeReference<List<BillPayTestData>>() {
				}, BillPayTestData::getKey, key);
	}
}