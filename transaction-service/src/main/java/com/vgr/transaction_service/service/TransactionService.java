package com.vgr.transaction_service.service;

import java.math.BigDecimal;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vgr.transaction_service.dto.TransactionRequest;
import com.vgr.transaction_service.entity.Account;
import com.vgr.transaction_service.entity.Transaction;
import com.vgr.transaction_service.exception.InsufficientBalanceException;
import com.vgr.transaction_service.exception.ResourceNotFoundException;
import com.vgr.transaction_service.repository.AccountFeignClient;
import com.vgr.transaction_service.repository.AccountRepository;
import com.vgr.transaction_service.repository.TransactionRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private HttpServletRequest request;

    private String getToken() {
        return request.getHeader("Authorization");
    }

    // ================= TRANSFER =================

    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "transferFallBack")
    public String transfer(TransactionRequest req) {

        log.info("********* Account Transfer Starts ***********");

        String token = getToken();

        Account from = accountFeignClient.getAccountByNumber(req.getFromAccount(), token)
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));

        Account to = accountFeignClient.getAccountByNumber(req.getToAccount(), token)
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        if (from.getBalance().compareTo(req.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        from.setBalance(from.getBalance().subtract(req.getAmount()));
        to.setBalance(to.getBalance().add(req.getAmount()));

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction tx = new Transaction();
        tx.setFromAccount(from.getAccountNumber());
        tx.setToAccount(to.getAccountNumber());
        tx.setAmount(req.getAmount());
        tx.setType("TRANSFER");

        transactionRepository.save(tx);

        log.info("********* Account Transfer End ***********");

        return "Transfer successful";
    }

    // ================= DEPOSIT =================

    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "depositFallBack")
    public String deposit(String accountNumber, BigDecimal amount) {

        String token = getToken();

        Account account = accountFeignClient.getAccountByNumber(accountNumber, token)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setToAccount(accountNumber);
        tx.setAmount(amount);
        tx.setType("DEPOSIT");

        transactionRepository.save(tx);

        return "Deposit successful";
    }

    // ================= WITHDRAW =================

    @Transactional
    @CircuitBreaker(name = "accountService", fallbackMethod = "withdrawFallBack")
    public String withdraw(String accountNumber, BigDecimal amount) {

        String token = getToken();

        Account account = accountFeignClient.getAccountByNumber(accountNumber, token)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setFromAccount(accountNumber);
        tx.setAmount(amount);
        tx.setType("WITHDRAW");

        transactionRepository.save(tx);

        return "Withdraw successful";
    }

    // ================= FALLBACK METHODS =================

    public String transferFallBack(TransactionRequest request, Throwable ex) {

        log.error("Account service is down. Executing fallback. Reason: {}", ex.getMessage());

        return "Transfer service temporarily unavailable. Please try again later.";
    }

    public String depositFallBack(String accountNumber, BigDecimal amount, Throwable ex) {

        log.error("Account service is down. Executing fallback. Reason: {}", ex.getMessage());

        return "Deposit service temporarily unavailable. Please try again later.";
    }

    public String withdrawFallBack(String accountNumber, BigDecimal amount, Throwable ex) {

        log.error("Account service is down. Executing fallback. Reason: {}", ex.getMessage());

        return "Withdraw service temporarily unavailable. Please try again later.";
    }
}