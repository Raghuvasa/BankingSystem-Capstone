package com.vgr.transaction_service.repository;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.vgr.transaction_service.entity.Account;

@FeignClient(name = "account-service", url = "http://localhost:8081")
public interface AccountFeignClient {

	@GetMapping("/accounts/accountNumber/{accountNumber}")
	Optional<Account> getAccountByNumber(@PathVariable String accountNumber,
			@RequestHeader("Autherization") String token);
}
