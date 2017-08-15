package com.revolut.banking.service;

import com.revolut.banking.domain.Customer;
import com.revolut.banking.dto.CustomerDto;
import com.revolut.banking.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer find(String customerId){
        return repository.find(customerId);
    }

    public Customer create(CustomerDto customerDto) {
        Customer customer = Customer.newCustomer(customerDto);
        repository.add(customer);
        return customer;
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }
}
