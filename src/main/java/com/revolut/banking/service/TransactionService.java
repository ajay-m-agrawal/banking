package com.revolut.banking.service;

import com.revolut.banking.domain.Account;
import com.revolut.banking.domain.Transaction;
import com.revolut.banking.domain.Transaction.TransactionBuilder;
import com.revolut.banking.domain.TransactionStatus;
import com.revolut.banking.domain.TransactionType;
import com.revolut.banking.dto.TransactionDto;
import com.revolut.banking.repository.TransactionRepository;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.revolut.banking.domain.TransactionStatus.PENDING;
import static com.revolut.banking.domain.TransactionType.DEPOSIT;
import static com.revolut.banking.domain.TransactionType.TRANSFER;

public class TransactionService {
    TransactionRepository transactionRepository;
    AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<Transaction> findAll(String customerId, String accountId) {
        return transactionRepository.findAll(customerId, accountId);
    }

    public List<Transaction> findAll(String customerId, String accountId, TransactionStatus transactionStatus) {
        return transactionRepository.findAll(customerId, accountId, transactionStatus);
    }

    public Transaction find(String customerId, String accountId, String transactionId) {
        Transaction transaction = transactionRepository.find(transactionId);
        if(transaction.getAccountId().equals(accountId) && transaction.getCustomerId().equals(customerId)){
            return transaction;
        }else{
            throw new IllegalArgumentException("transaction : " + transactionId
                    + " does not belong to customer : " + customerId
                    + " and account : " + accountId);
        }
    }

    public List<Transaction> process(TransactionDto transactionDto, String sourceCustomerId, String sourceAccountId) {
        Map<String, Account> accounts = validateRequest(transactionDto, sourceCustomerId, sourceAccountId);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(createTransaction(transactionDto, accounts.get("source"), transactionDto.type));
        if(TRANSFER.equals(transactionDto.type)){
            transactions.add(createTransaction(transactionDto, accounts.get("destination"), DEPOSIT));
        }
        return transactions;
    }

    private Map<String, Account> validateRequest(TransactionDto transactionDto, String sourceCustomerId, String sourceAccountId) {
        Map<String, Account> accounts = new HashMap<String, Account>();
        Account sourceAccount = accountService.find(sourceCustomerId, sourceAccountId);
        Account destinationAccount = null;
        if(TransactionType.TRANSFER.equals(transactionDto.type) || TransactionType.WITHDRAWAL.equals(transactionDto.type)){
            validateAccountBalance(sourceAccount, transactionDto.value);
            if(TransactionType.TRANSFER.equals(transactionDto.type)){
                if(StringUtils.isEmpty(transactionDto.destinationAccountId) || StringUtils.isEmpty(transactionDto.destinationCustomerId)){
                    throw new IllegalArgumentException("Destination account details required");
                }else{
                    destinationAccount = accountService.find(transactionDto.destinationCustomerId, transactionDto.destinationAccountId);
                }
            }
        }
        accounts.put("source", sourceAccount);
        if(destinationAccount != null) {
            accounts.put("destination", destinationAccount);
        }

        return accounts;
    }

    private void validateAccountBalance(Account account, BigDecimal value) {
        if(account.getAvailableBalance().compareTo(value) < 0){
            throw new IllegalArgumentException("Not enough balance in account : " + account.getId());
        }
    }

    private Transaction createTransaction(TransactionDto transactionDto, Account account, TransactionType type) {
        Transaction transaction = new TransactionBuilder()
                .withCustomer(account.getCustomerId())
                .withAccount(account.getId())
                .withType(type)
                .withValue(DEPOSIT.equals(type)?transactionDto.value:transactionDto.value.negate())
                .withDescription(transactionDto.description)
                .withStatus(PENDING)
                .build();
        transactionRepository.add(transaction);
        return transaction;
    }
}
