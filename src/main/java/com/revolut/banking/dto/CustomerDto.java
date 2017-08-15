package com.revolut.banking.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class CustomerDto {

    @NotEmpty
    public String firstName;
    @NotEmpty
    public String lastName;
}
