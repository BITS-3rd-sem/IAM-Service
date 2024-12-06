package com.docappointment.iam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
		ApiResponse error = new ApiResponse("error", exception.getMessage());
		
		return new ResponseEntity<ApiResponse>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception){
		ApiResponse error = new ApiResponse("error", exception.getMessage());
		
		return new ResponseEntity<ApiResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
		Map<String,String> response = new HashMap<>();
		response.put("status", "error");
		response.put("message", "Invalid fields");

		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			response.put(fieldName, message);
			
		});

		return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ApiResponse> handleAuthenticationException(Exception ex) {

		ApiResponse error = new ApiResponse("error", "Inavlid email or password");
		return new ResponseEntity<ApiResponse>(error, HttpStatus.UNAUTHORIZED);
    }

	@ExceptionHandler({ InvalidDataException.class })
	public ResponseEntity<ApiResponse> handleInvalidDataException(InvalidDataException ex) {

		ApiResponse error = new ApiResponse("error", ex.getMessage());
		return new ResponseEntity<ApiResponse>(error, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

		ApiResponse error = new ApiResponse("error", "Invalid data provided");
		return new ResponseEntity<ApiResponse>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {

		ApiResponse error = new ApiResponse("error", "Unknown error occured");
		return new ResponseEntity<ApiResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
