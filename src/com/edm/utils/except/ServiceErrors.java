package com.edm.utils.except;

/**
 * Service层公用的Exception.
 * 
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class ServiceErrors extends RuntimeException {

	private static final long serialVersionUID = 8624538688952462317L;

	public ServiceErrors() {
		super();
	}

	public ServiceErrors(String message) {
		super(message);
	}

	public ServiceErrors(Throwable cause) {
		super(cause);
	}

	public ServiceErrors(String message, Throwable cause) {
		super(message, cause);
	}
}
