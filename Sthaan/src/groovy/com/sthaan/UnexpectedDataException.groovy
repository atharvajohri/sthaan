package com.sthaan

public class UnexpectedDataException extends Exception {
	
	public UnexpectedDataException() {}
	
	public UnexpectedDataException(String message) {
		super(message);
	}
	
	public UnexpectedDataException(ArrayList parameters) {
		super("Missing some/all required parameters: " + parameters.join(", "));
	}

}
