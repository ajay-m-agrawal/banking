package com.revolut.banking.dto;

import com.revolut.banking.domain.TransactionType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransactionDto {

    public String destinationCustomerId;
    public String destinationAccountId;
    @NotNull
    public TransactionType type;
    @NotNull
    public BigDecimal value;
    @NotEmpty
    public String description;
}
