package com.vgr.account_service.exception;

import java.time.LocalDateTime;


public class ErrorResponse {

//    private LocalDateTime timestamp;
    private int status;
    private String message;

	/*
	 * public ErrorResponse(LocalDateTime timestamp, int status, String message) {
	 * // this.timestamp = timestamp; this.status = status; this.message = message;
	 * }
	 */
    
    
	/**
	 * @return the timestamp
	 */
	/*
	 * public LocalDateTime getTimestamp() { return timestamp; }
	 */
	public ErrorResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	/*
	 * public void setTimestamp(LocalDateTime timestamp) { this.timestamp =
	 * timestamp; }
	 */
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
    

    // getters
}