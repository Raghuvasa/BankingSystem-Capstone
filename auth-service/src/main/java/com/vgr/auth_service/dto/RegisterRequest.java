package com.vgr.auth_service.dto;

public class RegisterRequest {
	public String username;
	public String password;
	public String userRole; // ROLE_CUSTOMER, ROLE_ADMIN
	public String email;
	public String mobileNumber;
}
