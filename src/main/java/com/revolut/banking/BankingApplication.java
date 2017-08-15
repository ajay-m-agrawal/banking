package com.revolut.banking;


import com.revolut.banking.repository.AccountRepository;
import com.revolut.banking.repository.CustomerRepository;
import com.revolut.banking.repository.TransactionRepository;
import com.revolut.banking.resources.AccountResource;
import com.revolut.banking.resources.CustomerResource;
import com.revolut.banking.resources.TransactionResource;
import com.revolut.banking.service.AccountService;
import com.revolut.banking.service.CustomerService;
import com.revolut.banking.service.TransactionService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.dizitart.no2.Nitrite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankingApplication extends Application<BankingConfiguration>{

    private static  final Logger LOGGER = LoggerFactory.getLogger(BankingApplication.class);

    public static void main(String[] args) throws Exception {
        new BankingApplication().run(args);
    }


    public void run(BankingConfiguration bankingConfiguration, Environment environment) throws Exception {
        LOGGER.info("Registering REST resources");
        Nitrite db = Nitrite.builder().openOrCreate();

        final CustomerRepository customerRepository = new CustomerRepository(db);
        final AccountRepository accountRepository = new AccountRepository(db);
        final TransactionRepository transactionRepository = new TransactionRepository(db);

        final CustomerService customerService = new CustomerService(customerRepository);
        final AccountService accountsService = new AccountService(accountRepository, transactionRepository);
        final TransactionService transactionService = new TransactionService(transactionRepository, accountsService);

        final CustomerResource customerResource = new CustomerResource(customerService);
        final AccountResource accountResource = new AccountResource(accountsService);
        final TransactionResource transactionResource = new TransactionResource(transactionService);

        environment.jersey().register(customerResource);
        environment.jersey().register(accountResource);
        environment.jersey().register(transactionResource);


    }
}
