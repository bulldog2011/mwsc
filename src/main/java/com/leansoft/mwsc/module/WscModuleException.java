package com.leansoft.mwsc.module;

/**
 * Exception may thrown by client module
 * 
 * @author bulldog
 *
 */
public class WscModuleException extends Exception {
	private static final long serialVersionUID = 1L;

	public WscModuleException() {
	}

	public WscModuleException(String message) {
		super(message);
	}

	public WscModuleException(Throwable cause) {
		super(cause);
	}

	public WscModuleException(String message, Throwable cause) {
		super(message, cause);
	}
}
