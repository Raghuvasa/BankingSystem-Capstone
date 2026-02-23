package com.vgr.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vgr.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
