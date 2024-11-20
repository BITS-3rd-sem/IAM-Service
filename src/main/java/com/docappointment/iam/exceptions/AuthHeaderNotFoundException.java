package com.docappointment.iam.exceptions;

public class AuthHeaderNotFoundException extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	public AuthHeaderNotFoundException() {
		super("Auth Header not found, sign in again !!");
	}
	
	public AuthHeaderNotFoundException(String message) {
		super(message);
	}
}
