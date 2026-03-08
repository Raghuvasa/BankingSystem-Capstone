package com.vgr.account_service.service;

import com.vgr.account_service.entity.Account;
import com.vgr.account_service.exception.ResourceNotFoundException;
import com.vgr.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

	private final AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	// CREATE
	public Account createAccount(Account account) {
		account.setAccountNumber(generateAccountNumber());
		return accountRepository.save(account);
	}

	// READ
	public Account getAccountById(Long id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
	}

	public List<Account> getAccountsByCustomer(Long customerId) {
		List<Account> accounts = accountRepository.findByCustomerId(customerId);

		if (accounts.isEmpty()) {
			throw new ResourceNotFoundException("No accounts found for customer id: " + customerId);
		}

		return accounts;
	}

	// UPDATE
	public Account updateAccount(Long id, Account updated) {
		Account account = getAccountById(id);
		account.setAccountType(updated.getAccountType());
		account.setStatus(updated.getStatus());
		return accountRepository.save(account);
	}

	// DELETE (Soft delete)
	public void closeAccount(Long id) {
		Account account = getAccountById(id);
		account.setStatus("CLOSED");
		accountRepository.save(account);
	}

	public Account getAccountByNumber(String accountNumber) {
		Account account = accountRepository.findByAccountNumber(accountNumber);

		if (account == null) {
			throw new ResourceNotFoundException("Account not found with number: " + accountNumber);
		}

		return account;
	}

	private String generateAccountNumber() {
		return "AC" + System.currentTimeMillis();
	}
}