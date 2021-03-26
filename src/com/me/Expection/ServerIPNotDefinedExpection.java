package com.me.Expection;

public class ServerIPNotDefinedExpection extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2687288921909591067L;

	public ServerIPNotDefinedExpection() {
	}

	public ServerIPNotDefinedExpection(String arg0) {
		super(arg0);
	}

	public ServerIPNotDefinedExpection(Throwable arg0) {
		super(arg0);
	}

	public ServerIPNotDefinedExpection(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServerIPNotDefinedExpection(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
