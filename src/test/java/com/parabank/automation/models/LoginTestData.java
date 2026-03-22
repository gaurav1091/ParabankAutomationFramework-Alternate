package com.parabank.automation.models;

import com.parabank.automation.config.SensitiveDataResolver;

public class LoginTestData {

	private String key;
	private String username;
	private String password;

	public LoginTestData() {
	}

	public LoginTestData(String key, String username, String password) {
		this.key = key;
		this.username = username;
		this.password = password;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return SensitiveDataResolver.resolveCredentialValue(username, "Login test data username for key: " + key);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return SensitiveDataResolver.resolveCredentialValue(password, "Login test data password for key: " + key);
	}

	public void setPassword(String password) {
		this.password = password;
	}
}