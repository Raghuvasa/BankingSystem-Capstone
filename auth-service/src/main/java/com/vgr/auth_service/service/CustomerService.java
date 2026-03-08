package com.vgr.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vgr.auth_service.dto.CustomerRequest;
import com.vgr.auth_service.dto.CustomerResponse;
import com.vgr.auth_service.entity.Customer;
import com.vgr.auth_service.exception.CustomException;
import com.vgr.auth_service.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerResponse register(CustomerRequest request) {

        if (repository.existsByEmail(request.email))
            throw new CustomException("Email already registered", 409);

        if (repository.existsByMobileNumber(request.mobileNumber))
            throw new CustomException("Mobile number already registered", 409);

        if (repository.existsByUsername(request.username))
            throw new CustomException("Username already exists", 409);

        Customer customer = new Customer();
        customer.setFirstName(request.firstName);
        customer.setLastName(request.lastName);
        customer.setUsername(request.username);
        customer.setPassword(passwordEncoder.encode(request.password));
        customer.setEmail(request.email);
        customer.setMobileNumber(request.mobileNumber);
        customer.setDateOfBirth(request.dateOfBirth);
        customer.setAddress(request.address);
        customer.setCity(request.city);
        customer.setState(request.state);
        customer.setPincode(request.pincode);

        Customer saved = repository.save(customer);

        CustomerResponse response = new CustomerResponse();
        response.id = saved.getId();
        response.username = saved.getUsername();
        response.email = saved.getEmail();
        response.status = saved.getStatus().name();

        return response;
    }
}
