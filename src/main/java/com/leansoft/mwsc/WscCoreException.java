package com.leansoft.mwsc;

/**
 * General WSC engine exception
 * 
 * @author bulldog
 *
 */
public class WscCoreException extends Exception {

	private static final long serialVersionUID = 1L;

	public WscCoreException() {
	}

	public WscCoreException(String message) {
		super(message);
	}

	public WscCoreException(Throwable cause) {
		super(cause);
	}

	public WscCoreException(String message, Throwable cause) {
		super(message, cause);
	}

}
