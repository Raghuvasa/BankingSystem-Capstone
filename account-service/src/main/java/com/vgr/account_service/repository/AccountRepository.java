package com.vgr.account_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vgr.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByCustomerId(Long customerId);

    Account findByAccountNumber(String accountNumber);
}