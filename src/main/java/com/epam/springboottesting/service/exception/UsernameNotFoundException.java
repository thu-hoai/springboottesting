package com.epam.springboottesting.service.exception;

public class UsernameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameNotFoundException(String exception) {
		super(exception);

	}
}
