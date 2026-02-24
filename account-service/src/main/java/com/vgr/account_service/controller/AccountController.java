package com.vgr.account_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vgr.account_service.entity.Account;
import com.vgr.account_service.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService service;

    @PostMapping
    public Account create(@RequestBody Account account) {
    	log.info("**********Creating Account***********");
        return service.createAccount(account);
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable Long id) {
    	log.info("Fetching account with id: {}", id);

    	if (id <= 0) {
            log.warn("Invalid account id received: {}", id);
            throw new IllegalArgumentException("Invalid account id");
        }
        return service.getAccount(id);
    }
}