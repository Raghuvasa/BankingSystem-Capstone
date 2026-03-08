package com.vgr.transaction_service.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vgr.transaction_service.dto.TransactionRequest;
import com.vgr.transaction_service.entity.Account;
import com.vgr.transaction_service.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransactionRequest request) {
        return transactionService.transfer(request);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestBody Account account) {
        return transactionService.deposit(account.getAccountNumber(), account.getBalance());
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody Account account) {
        return transactionService.withdraw(account.getAccountNumber(), account.getBalance());
    }
}