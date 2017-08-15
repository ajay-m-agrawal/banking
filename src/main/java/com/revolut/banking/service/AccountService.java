package com.revolut.banking.service;

import com.revolut.banking.domain.Account;
import com.revolut.banking.domain.Transaction;
import com.revolut.banking.dto.AccountDto;
import com.revolut.banking.repository.AccountRepository;
import com.revolut.banking.repository.TransactionRepository;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.revolut.banking.domain.Account.newAccount;
import static com.revolut.banking.domain.TransactionStatus.PENDING;

public class AccountService {
    final private AccountRepository repository;
    final private TransactionRepository transactionRepository;

    public AccountService(AccountRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    public List<Account> findAll(String customerId) {
        List<Account> accountsFromDb = repository.findAll(customerId);
        List<Account> accounts = new ArrayList<Account>();
        for (Account account: accountsFromDb) {
            DateTime balanceValidDate = DateTime.now();
            accounts.add(newAccount(account, getAvailableBalance(customerId, account.getId(), account.getBalance()), balanceValidDate));
        }
        return accounts;
    }

    public Account find(String customerId, String accountId) {
        Account accountFromDb = repository.find(accountId);
        DateTime balanceValidDate = DateTime.now();
        if(accountFromDb.getCustomerId() != null && accountFromDb.getCustomerId().equals(customerId)){
            return newAccount(accountFromDb, getAvailableBalance(customerId, accountId, accountFromDb.getBalance()), balanceValidDate);
        }else {
            throw new IllegalArgumentException ("Account : " + accountId + ", does not belong to customer : " + customerId);
        }
    }

    private BigDecimal getAvailableBalance(String customerId, String accountId, BigDecimal balance) {
        List<Transaction> pendingTransactions = transactionRepository.findAll(customerId, accountId, PENDING);
        BigDecimal balanceDelta = balance;
        for (Transaction transaction: pendingTransactions) {
            balanceDelta = balanceDelta.add(transaction.getValue());
        }
        return balanceDelta;

    }

    public Account create(String customerId, AccountDto accountDto) {
        Account account = newAccount(accountDto, customerId);
        repository.add(account);
        return account;
    }
}
