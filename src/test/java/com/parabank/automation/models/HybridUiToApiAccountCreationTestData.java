package com.parabank.automation.models;

public class HybridUiToApiAccountCreationTestData {

	private String key;
	private String loginKey;
	private String openNewAccountKey;

	public HybridUiToApiAccountCreationTestData() {
	}

	public HybridUiToApiAccountCreationTestData(String key, String loginKey, String openNewAccountKey) {
		this.key = key;
		this.loginKey = loginKey;
		this.openNewAccountKey = openNewAccountKey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getOpenNewAccountKey() {
		return openNewAccountKey;
	}

	public void setOpenNewAccountKey(String openNewAccountKey) {
		this.openNewAccountKey = openNewAccountKey;
	}
}