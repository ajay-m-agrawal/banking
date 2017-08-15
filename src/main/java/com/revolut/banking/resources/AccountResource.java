package com.revolut.banking.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.banking.domain.Account;
import com.revolut.banking.dto.AccountDto;
import com.revolut.banking.repository.AccountRepository;
import com.revolut.banking.service.AccountService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers/{customerId}/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    AccountService service;

    public AccountResource(AccountService service) {
        this.service = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<Account> getAccounts(@PathParam("customerId") @NotEmpty String customerId) {
        return service.findAll(customerId);
    }

    @GET
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Account getAccount(@PathParam("customerId") @NotEmpty String customerId, @PathParam("accountId") @NotEmpty String accountId) {
        return service.find(customerId, accountId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public Account createAccount(@PathParam("customerId") @NotEmpty String customerId, @Valid AccountDto accountDto) {
        return service.create(customerId, accountDto);
    }
}
