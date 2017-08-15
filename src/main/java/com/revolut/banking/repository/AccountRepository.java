package com.revolut.banking.repository;

import com.revolut.banking.domain.Account;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

public class AccountRepository {
    private final ObjectRepository<Account> repository;
    private Nitrite nitriteDb;

    public AccountRepository(Nitrite db) {
        this.nitriteDb = db;
        repository = nitriteDb.getRepository(Account.class);
    }

    public Account find(String accountId) {
        return repository.find(
                and(
                   eq("id", accountId)
                )
        ).firstOrDefault();
    }

    public List<Account> findAll(String customerId) {
        return repository.find(
                and(
                        eq("customerId", customerId)
                )
        ).toList();
    }

    public void add(Account account) {
        repository.insert(account);
    }
}
