package com.epam.springbootpracticing.service.exception;

public class NonExistentUserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NonExistentUserException(String exception) {
		super(exception);

	}
}
