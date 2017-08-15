package com.revolut.banking.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.banking.domain.Transaction;
import com.revolut.banking.domain.TransactionStatus;
import com.revolut.banking.dto.TransactionDto;
import com.revolut.banking.service.TransactionService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers/{customerId}/accounts/{accountId}/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    TransactionService transactionService;

    public TransactionResource(TransactionService service) {
        this.transactionService = service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<Transaction> getTransactions(@PathParam("customerId") @NotEmpty String customerId,
                                             @PathParam("accountId") @NotEmpty String accountId) {
        return transactionService.findAll(customerId, accountId);
    }

    @GET
    @Path("{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Transaction getTransaction(@PathParam("customerId") @NotEmpty String customerId,
                                      @PathParam("accountId") @NotEmpty String accountId,
                                      @PathParam("transactionId") @NotEmpty String transactionId) {

        return transactionService.find(customerId, accountId, transactionId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public List<Transaction> createTransaction(@PathParam("customerId") @NotEmpty String sourceCustomerId,
                                               @PathParam("accountId") @NotEmpty String sourceAccountId,
                                               @Valid TransactionDto transactionDto) {
        return transactionService.process(transactionDto, sourceCustomerId, sourceAccountId);
    }
}
