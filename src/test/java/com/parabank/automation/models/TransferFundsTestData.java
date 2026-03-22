package com.parabank.automation.models;

public class TransferFundsTestData {

	private String key;
	private String amount;

	public TransferFundsTestData() {
	}

	public TransferFundsTestData(String key, String amount) {
		this.key = key;
		this.amount = amount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}