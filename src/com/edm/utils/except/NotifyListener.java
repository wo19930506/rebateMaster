package com.edm.utils.except;

public class NotifyListener extends RuntimeException {

	private static final long serialVersionUID = -3084553941355577954L;

	public NotifyListener() {
		super();
	}

	public NotifyListener(String message) {
		super(message);
	}

	public NotifyListener(Throwable cause) {
		super(cause);
	}

	public NotifyListener(String message, Throwable cause) {
		super(message, cause);
	}
}