package com.vgr.account_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vgr.account_service.entity.Account;
import com.vgr.account_service.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// CREATE
	@PostMapping
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {
		return ResponseEntity.ok(accountService.createAccount(account));
	}

	// READ by ID

	@GetMapping("/{id}")
	public ResponseEntity<Account> getAccount(@AuthenticationPrincipal Jwt jwt,
												@PathVariable Long id) {
		String username = jwt.getSubject();
		return ResponseEntity.ok(accountService.getAccountById(id));
	}

	@GetMapping("/accountNumber/{accountNumber}")
	public Account getAccountByNumber(@PathVariable String accountNumber) {
		return accountService.getAccountByNumber(accountNumber);
	}

	// READ by Customer
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Account>> getAccountsByCustomer(@PathVariable Long customerId) {
		return ResponseEntity.ok(accountService.getAccountsByCustomer(customerId));
	}

	// UPDATE
	@PutMapping("/update/account/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
		return ResponseEntity.ok(accountService.updateAccount(id, account));
	}

	// DELETE (Close Account)
	@DeleteMapping("/{id}")
	public ResponseEntity<String> closeAccount(@PathVariable Long id) {
		accountService.closeAccount(id);
		return ResponseEntity.ok("Account closed successfully");
	}
}