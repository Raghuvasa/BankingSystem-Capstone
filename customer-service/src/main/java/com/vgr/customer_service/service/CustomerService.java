package com.vgr.customer_service.service;

import org.springframework.stereotype.Service;

import com.vgr.customer_service.dto.CustomerRequest;
import com.vgr.customer_service.dto.CustomerResponse;
import com.vgr.customer_service.entity.Customer;
import com.vgr.customer_service.exception.CustomerAlreadyExistsException;
import com.vgr.customer_service.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerResponse register(CustomerRequest request) {

        if (repository.existsByEmail(request.email))
            throw new CustomerAlreadyExistsException("Email already registered");

        if (repository.existsByMobileNumber(request.mobileNumber))
            throw new CustomerAlreadyExistsException("Mobile number already registered");

        if (repository.existsByUsername(request.username))
            throw new CustomerAlreadyExistsException("Username already exists");

        Customer customer = new Customer();
        customer.setFirstName(request.firstName);
        customer.setLastName(request.lastName);
        customer.setUsername(request.username);
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
