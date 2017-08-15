package com.revolut.banking.domain;

import java.math.BigDecimal;

import static java.util.UUID.randomUUID;

public class Transaction {

    private String id;
    private TransactionType type;
    private BigDecimal value;
    private String description;
    private String accountId;
    private String customerId;
    private TransactionStatus status;

    public Transaction() {
    }

    private Transaction(String id, TransactionType type, BigDecimal value, String description, String accountId, String customerId, TransactionStatus status) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.description = description;
        this.accountId = accountId;
        this.customerId = customerId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public static class TransactionBuilder{

        private String id = randomUUID().toString();
        private TransactionType type;
        private BigDecimal value;
        private String description;
        private String accountId;
        private String customerId;
        private TransactionStatus status;

        public TransactionBuilder withType(TransactionType type){
            this.type = type;
            return this;
        }

        public TransactionBuilder withDescription(String description){
            this.description = description;
            return this;
        }

        public TransactionBuilder withValue(BigDecimal value){
            this.value = value;
            return this;
        }

        public TransactionBuilder withAccount(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public TransactionBuilder withCustomer(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public TransactionBuilder withStatus(TransactionStatus status) {
            this.status = status;
            return this;
        }

        public Transaction build(){
            return new Transaction(id, type, value, description, accountId, customerId, status);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!id.equals(that.id)) return false;
        if (type != that.type) return false;
        if (!value.equals(that.value)) return false;
        if (!description.equals(that.description)) return false;
        if (!accountId.equals(that.accountId)) return false;
        if (!customerId.equals(that.customerId)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + accountId.hashCode();
        result = 31 * result + customerId.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
