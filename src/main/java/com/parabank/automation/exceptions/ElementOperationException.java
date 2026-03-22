package com.parabank.automation.exceptions;

public class ElementOperationException extends RuntimeException {

	public ElementOperationException(String message) {
		super(message);
	}

	public ElementOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}