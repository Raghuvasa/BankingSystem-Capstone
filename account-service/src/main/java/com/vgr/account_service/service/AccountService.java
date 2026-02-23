package com.vgr.account_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vgr.account_service.controller.AccountController;
import com.vgr.account_service.entity.Account;
import com.vgr.account_service.repository.AccountRepository;

@Service
public class AccountService {
	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	
	@Autowired
	private AccountRepository repository;

	/**
	 * 
	 * @param account
	 * @return
	 */
	public Account createAccount(Account account) {
		account.setCustomerId(account.getCustomerId());
        account.setStatus(account.getStatus());
        account.setBalance(account.getBalance());
        return repository.save(account);
    }

    public Account getAccount(Long id) {
    	    	
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void updateBalance(Long id, Double amount) {
        Account acc = getAccount(id);
        acc.setBalance(acc.getBalance() + amount);
        repository.save(acc);
    }
}
