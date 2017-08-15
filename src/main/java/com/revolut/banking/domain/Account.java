package com.revolut.banking.domain;

import com.revolut.banking.dto.AccountDto;
import org.joda.time.DateTime;

import java.math.BigDecimal;

import static java.util.UUID.randomUUID;

public class Account {
    private String id;
    private String customerId;
    private AccountType type;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private DateTime balanceValidDate;

    public static Account newAccount(AccountDto accountDto, String customerId){
        return new AccountBuilder()
                .withCustomerId(customerId)
                .withType(accountDto.type)
                .withBalance(BigDecimal.valueOf(0))
                .withAvailableBalance(BigDecimal.valueOf(0))
                .build();
    }

    public static Account newAccount(Account account, BigDecimal availableBalance, DateTime balanceValidDate) {
        return new AccountBuilder()
                .withId(account.id)
                .withCustomerId(account.customerId)
                .withType(account.type)
                .withBalance(account.balance)
                .withAvailableBalance(availableBalance)
                .withBalanceValidDate(balanceValidDate)
                .build();
    }

    private Account(String id, String customerId, AccountType type, BigDecimal balance, BigDecimal availableBalance, DateTime balanceValidDate) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.balance = balance;
        this.availableBalance = availableBalance;
        this.balanceValidDate = balanceValidDate;
    }

    public Account() {
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public DateTime getBalanceValidDate() {
        return balanceValidDate;
    }

    public static class AccountBuilder {
        private String id = randomUUID().toString();
        private String customerId;
        private AccountType type;
        private BigDecimal balance = new BigDecimal(0);
        private BigDecimal availableBalance = new BigDecimal(0);
        private DateTime balanceValidDate;

        public AccountBuilder withId(String id){
            this.id = id;
            return this;
        }

        public AccountBuilder withCustomerId(String customerId){
            this.customerId = customerId;
            return this;
        }

        public AccountBuilder withType(AccountType type){
            this.type = type;
            return this;
        }

        public AccountBuilder withBalance(BigDecimal balance){
            this.balance = balance;
            return this;
        }

        public AccountBuilder withAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
            return this;
        }

        public AccountBuilder withBalanceValidDate(DateTime balanceValidDate) {
            this.balanceValidDate = balanceValidDate;
            return this;
        }

        public Account build(){
            if(id == null){
                id = randomUUID().toString();
            }
            return new Account(id, customerId, type, balance, availableBalance, balanceValidDate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;
        if (!customerId.equals(account.customerId)) return false;
        if (type != account.type) return false;
        if (!balance.equals(account.balance)) return false;
        return availableBalance.equals(account.availableBalance);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + customerId.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + availableBalance.hashCode();
        return result;
    }
}
