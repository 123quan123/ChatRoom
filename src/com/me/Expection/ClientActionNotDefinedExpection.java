package com.me.Expection;

public class ClientActionNotDefinedExpection extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7598469932315080397L;

	public ClientActionNotDefinedExpection() {
	}

	public ClientActionNotDefinedExpection(String arg0) {
		super(arg0);
	}

	public ClientActionNotDefinedExpection(Throwable arg0) {
		super(arg0);
	}

	public ClientActionNotDefinedExpection(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ClientActionNotDefinedExpection(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
