package com.edm.utils.except;

import org.springframework.security.core.AuthenticationException;

public class AuthErrors extends AuthenticationException {

	private static final long serialVersionUID = 8139329116564449847L;

	public AuthErrors(String message) {
		super(message);
	}

	public AuthErrors(String message, Throwable cause) {
		super(message, cause);
	}
}
