package com.manzoli.diff.service.exceptions;

public class MissingJsonException extends Exception {

	private static final long serialVersionUID = 6183547667087109282L;

	public MissingJsonException() {

	}
	
	public MissingJsonException(String message) {
		super(message);
	}

}
