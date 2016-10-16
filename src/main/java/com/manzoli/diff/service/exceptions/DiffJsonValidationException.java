package com.manzoli.diff.service.exceptions;

public class DiffJsonValidationException extends Exception {

	private static final long serialVersionUID = -4201616114594750151L;
	
	public DiffJsonValidationException(){
		
	}
	
	public DiffJsonValidationException(String message){
		super(message);
	}

}
