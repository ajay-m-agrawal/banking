package com.revolut.banking.repository;

import com.revolut.banking.domain.Transaction;
import com.revolut.banking.domain.TransactionStatus;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

public class TransactionRepository {
    private final ObjectRepository<Transaction> repository;
    private Nitrite nitriteDb;

    public TransactionRepository(Nitrite db) {
        this.nitriteDb = db;
        repository = nitriteDb.getRepository(Transaction.class);
    }

    public List<Transaction> findAll(String customerId, String accountId, TransactionStatus transactionStatus) {
        return repository.find(
                and(
                        eq("accountId", accountId),
                        eq("customerId", customerId),
                        eq("status", transactionStatus)
                )
        ).toList();
    }

    public List<Transaction> findAll(String customerId, String accountId) {
        return repository.find(
                and(
                        eq("accountId", accountId),
                        eq("customerId", customerId)
                )
        ).toList();
    }

    public Transaction find(String transactionId) {
        return repository.find(
                and(
                        eq("id", transactionId)
                )
        ).firstOrDefault();
    }

    public void add(Transaction transaction) {
        repository.insert(transaction);
    }
}
