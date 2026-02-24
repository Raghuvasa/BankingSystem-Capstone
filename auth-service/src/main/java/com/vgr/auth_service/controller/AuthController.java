package com.vgr.auth_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.vgr.auth_service.dto.AuthResponse;
import com.vgr.auth_service.dto.LoginRequest;
import com.vgr.auth_service.dto.RegisterRequest;
import com.vgr.auth_service.entity.User;
import com.vgr.auth_service.repository.UserRepository;
import com.vgr.auth_service.service.UserService;
import com.vgr.auth_service.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
		log.info("User Registration Starts");
		userService.register(request);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody LoginRequest request) {

		log.info("------------Authenticating User---------"); 
		User user = repository.findByUsername(request.username)
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.password, user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(user.getUserName());
		return new AuthResponse(token);
	}
}
