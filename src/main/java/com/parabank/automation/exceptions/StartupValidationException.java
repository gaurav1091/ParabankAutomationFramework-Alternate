package com.parabank.automation.exceptions;

public class StartupValidationException extends RuntimeException {

	public StartupValidationException(String message) {
		super(message);
	}

	public StartupValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}