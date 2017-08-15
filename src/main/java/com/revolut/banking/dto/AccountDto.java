package com.revolut.banking.dto;

import com.revolut.banking.domain.AccountType;

import javax.validation.constraints.NotNull;


public class AccountDto {
    @NotNull
    public AccountType type;
}
