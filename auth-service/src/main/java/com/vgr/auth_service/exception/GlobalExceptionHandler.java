package com.vgr.auth_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {

		ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getStatus());

		return ResponseEntity.status(ex.getStatus()).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

		ErrorResponse error = new ErrorResponse("Something went wrong", 500);

		return ResponseEntity.status(500).body(error);
	}
}