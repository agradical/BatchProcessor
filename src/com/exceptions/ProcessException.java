package com.exceptions;

public class ProcessException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
