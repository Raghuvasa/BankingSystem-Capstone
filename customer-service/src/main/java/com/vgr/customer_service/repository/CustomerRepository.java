/**
 * 
 */
package com.vgr.customer_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vgr.customer_service.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobileNumber);

	boolean existsByUsername(String username);

	Optional<Customer> findByUsername(String username);
}