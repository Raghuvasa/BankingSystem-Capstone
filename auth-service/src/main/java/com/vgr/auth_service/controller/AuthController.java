package com.vgr.auth_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vgr.auth_service.dto.AuthResponse;
import com.vgr.auth_service.dto.LoginRequest;
import com.vgr.auth_service.dto.RegisterRequest;
import com.vgr.auth_service.entity.EmailOtp;
import com.vgr.auth_service.entity.User;
import com.vgr.auth_service.service.AuthService;
import com.vgr.auth_service.util.JwtUtil;

/**
 * @author Raghu
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

		log.info("Register request received for username: {}", request.username);

		User user = authService.register(request);

		log.info("User registered successfully: {}", request.username);
		
		authService.sendOtpEmail(user.getEmail());

		return ResponseEntity.ok("User registered successfully and sent email for otp verfication");
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody LoginRequest request) {

		log.info("Login attempt for username: {}", request.username);

		User user = authService.validateUser(request.username, request.password);

		log.info("User validated successfully: {}", request.username);

		List<String> roles = List.of("ROLE_USER");

		String token = jwtUtil.generateToken(user.getId(), user.getUsername(), roles);

		log.info("JWT token generated for userId: {}", user.getId());

		return new AuthResponse(token);
	}

	@PostMapping("/verifyOtp")
	public ResponseEntity<String> verifyEmailOtp(@RequestBody EmailOtp request) {

		log.info("OTP verification attempt for email: {}", request.getEmail());

		authService.verifyOtp(request.getEmail(), request.getOtp());

		log.info("Email verified successfully: {}", request.getEmail());

		return ResponseEntity.ok("Email verified successfully");
	}
}