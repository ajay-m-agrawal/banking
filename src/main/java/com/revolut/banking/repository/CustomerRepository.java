package com.revolut.banking.repository;

import com.revolut.banking.domain.Customer;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

public class CustomerRepository {
    private final ObjectRepository<Customer> repository;
    private Nitrite nitriteDb;

    public CustomerRepository(Nitrite db) {
        this.nitriteDb = db;
        repository = nitriteDb.getRepository(Customer.class);
    }

    public Customer find(String customerId) {
        return repository.find(
                and(
                   eq("id", customerId)
                )
        ).firstOrDefault();
    }

    public List<Customer> findAll() {
        return repository.find().toList();
    }

    public void add(Customer customer) {
        repository.insert(customer);
    }


}
