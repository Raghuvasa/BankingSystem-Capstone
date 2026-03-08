package com.vgr.auth_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vgr.auth_service.dto.CustomerRequest;
import com.vgr.auth_service.dto.CustomerResponse;
import com.vgr.auth_service.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse register(@RequestBody CustomerRequest request) {
    	log.info("Register in AuthService customer controller----------------------");
        return service.register(request);
    }
}
