package com.vgr.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vgr.auth_service.entity.EmailOtp;
import com.vgr.auth_service.entity.User;

public interface OtpRepository extends JpaRepository<EmailOtp, Long>{
	Optional<EmailOtp> findByEmail(String email);

}
