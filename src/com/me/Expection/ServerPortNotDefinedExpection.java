package com.me.Expection;

public class ServerPortNotDefinedExpection extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4630034192456169925L;

	public ServerPortNotDefinedExpection() {
	}

	public ServerPortNotDefinedExpection(String message) {
		super(message);
	}

	public ServerPortNotDefinedExpection(Throwable cause) {
		super(cause);
	}

	public ServerPortNotDefinedExpection(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerPortNotDefinedExpection(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
