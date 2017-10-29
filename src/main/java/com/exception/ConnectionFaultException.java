package com.exception;

public class ConnectionFaultException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionFaultException() {
		super();
	}

	public ConnectionFaultException(String object) {
		super(object);
	}

}
