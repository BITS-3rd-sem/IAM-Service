package com.docappointment.iam.exceptions;

public class ApiResponse {

	public String status;
	public String message;
	
	public ApiResponse(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getstatus() {
		return status;
	}
	public void setstatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}