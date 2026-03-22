package com.parabank.automation.exceptions;

public class ConfigFileException extends RuntimeException {

	public ConfigFileException(String message) {
		super(message);
	}

	public ConfigFileException(String message, Throwable cause) {
		super(message, cause);
	}
}