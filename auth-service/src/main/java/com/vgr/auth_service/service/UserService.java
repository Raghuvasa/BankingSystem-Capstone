package com.vgr.auth_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vgr.auth_service.controller.AuthController;
import com.vgr.auth_service.dto.RegisterRequest;
import com.vgr.auth_service.entity.User;
import com.vgr.auth_service.repository.UserRepository;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        if (repository.findByUsername(request.username).isPresent()) {
        	log.info("User already exists");
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUserName(request.username);
        user.setPassword(passwordEncoder.encode(request.password));
        
        log.info("Creating user in service class");
        return repository.save(user);
    }
}