package com.parabank.automation.models;

public class OpenNewAccountTestData {

	private String key;
	private String accountType;

	public OpenNewAccountTestData() {
	}

	public OpenNewAccountTestData(String key, String accountType) {
		this.key = key;
		this.accountType = accountType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
}