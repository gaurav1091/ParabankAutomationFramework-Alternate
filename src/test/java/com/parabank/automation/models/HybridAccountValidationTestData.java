package com.parabank.automation.models;

public class HybridAccountValidationTestData {

	private String key;
	private String loginKey;

	public HybridAccountValidationTestData() {
	}

	public HybridAccountValidationTestData(String key, String loginKey) {
		this.key = key;
		this.loginKey = loginKey;
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
}