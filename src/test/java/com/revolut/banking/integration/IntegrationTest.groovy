package com.revolut.banking.integration;

import com.revolut.banking.BankingApplication;
import com.revolut.banking.BankingConfiguration
import com.revolut.banking.domain.Account
import com.revolut.banking.domain.Customer
import com.revolut.banking.domain.Transaction
import com.revolut.banking.dto.AccountDto;
import com.revolut.banking.dto.CustomerDto
import com.revolut.banking.dto.TransactionDto
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType

import static com.revolut.banking.domain.AccountType.SAVINGS
import static com.revolut.banking.domain.TransactionType.DEPOSIT
import static com.revolut.banking.domain.TransactionType.TRANSFER
import static com.revolut.banking.domain.TransactionType.WITHDRAWAL
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("banking.yml");

    @ClassRule
    public static final DropwizardAppRule<BankingConfiguration> RULE = new DropwizardAppRule<BankingConfiguration>(
            BankingApplication.class, CONFIG_PATH);

    @BeforeClass
    public static void setUp() throws Exception {
    }

    @Test
    public void testCreatePerson() throws Exception {

        Customer createdCustomer = createCustomer("Ajay", "Agrawal")
        Customer readCustomer = getCustomer(createdCustomer.id)

        assertThat(createdCustomer.firstName).isEqualTo(readCustomer.firstName);
        assertThat(createdCustomer.lastName).isEqualTo(readCustomer.lastName);
        assertThat(createdCustomer.id).isEqualTo(readCustomer.id);
    }

    @Test
    public void testCreateAccount() throws Exception {

        Customer createdCustomer = createCustomer("Ajay", "Agrawal")
        Account account = createAccountFor(createdCustomer.id)

        assertThat(account.id).isNotNull();
        assertThat(account.type).isEqualTo(SAVINGS);
        assertThat(account.customerId).isEqualTo(createdCustomer.id);

        Account fetchedAccount = getAccount(createdCustomer.id, account.id)
        assertThat(account.id).isEqualTo(fetchedAccount.id);
        assertThat(account.type).isEqualTo(fetchedAccount.type);
        assertThat(account.customerId).isEqualTo(fetchedAccount.customerId);
        assertThat(account.balance).isEqualTo(fetchedAccount.balance);
        assertThat(account.availableBalance).isEqualTo(fetchedAccount.availableBalance);

    }

    @Test
    public void testMoneyTransfer() throws Exception {

        Customer sourceCustomer = createCustomer("Ajay", "Agrawal")
        Account sourceAccount = createAccountFor(sourceCustomer.id)

        Customer destinationCustomer = createCustomer("Ajay", "Agrawal")
        Account destinationAccount = createAccountFor(destinationCustomer.id)

        List<Transaction> transactions = depositMoney(sourceCustomer, sourceAccount, new BigDecimal(1000), "test deposit")
        assertThat(transactions.size()).isEqualTo(1)
        def updatedSourceAccount = getAccount(sourceCustomer.id, sourceAccount.id)
        assertThat(updatedSourceAccount.availableBalance).isEqualTo(new BigDecimal(1000))


        transactions = transferMoney(sourceCustomer, sourceAccount, destinationCustomer, destinationAccount, new BigDecimal(1000), "test transfer")
        assertThat(transactions.size()).isEqualTo(2)

        updatedSourceAccount = getAccount(sourceCustomer.id, sourceAccount.id)
        assertThat(updatedSourceAccount.availableBalance).isEqualTo(new BigDecimal(0))
        def updatedDestinationAccount = getAccount(destinationCustomer.id, destinationAccount.id)
        assertThat(updatedDestinationAccount.availableBalance).isEqualTo(new BigDecimal(1000))

        transactions = withdrawMoney(destinationCustomer, destinationAccount, new BigDecimal(1000), "test withdrawal")
        assertThat(transactions.size()).isEqualTo(1)
        updatedDestinationAccount = getAccount(destinationCustomer.id, destinationAccount.id)
        assertThat(updatedDestinationAccount.availableBalance).isEqualTo(new BigDecimal(0))
    }

    static List<Transaction> withdrawMoney(Customer customer, Account account, BigDecimal withdrawalValue, String description) {
        TransactionDto transactionDto = new TransactionDto(type: WITHDRAWAL, value: withdrawalValue, description: description)
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + customer.id + "/accounts/" + account.id + "/transactions")
                .request()
                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(List.class)
    }

    static List<Transaction> depositMoney(Customer customer, Account account, BigDecimal depositValue, String description) {
        TransactionDto transactionDto = new TransactionDto(type: DEPOSIT, value: depositValue, description: description)
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + customer.id + "/accounts/" + account.id + "/transactions")
                .request()
                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(List.class)
    }

    static List<Transaction> transferMoney(Customer sourceCustomer, Account sourceAccount, Customer destinationCustomer, Account destinationAccount, BigDecimal transferValue, String description) {
        TransactionDto transactionDto = new TransactionDto(type: TRANSFER, value: transferValue, destinationCustomerId: destinationCustomer.id, destinationAccountId: destinationAccount.id, description: description)
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + sourceCustomer.id + "/accounts/" + sourceAccount.id + "/transactions")
                .request()
                .post(Entity.entity(transactionDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(List.class)
    }

    static Account getAccount(String customerId, String accountId) {
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + customerId + "/accounts/" + accountId)
                .request()
                .get(Account.class)
    }

    static Account createAccountFor(String customerId) {
        AccountDto accountDto = new AccountDto(type: SAVINGS);
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + customerId + "/accounts/")
                .request()
                .post(Entity.entity(accountDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Account.class)
    }

    private static Customer getCustomer(Object customerId) {
        final Customer readCustomer = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/" + customerId)
                .request()
                .get(Customer.class);
        readCustomer
    }

    private static Customer createCustomer(String firstName, String lastName) {
        CustomerDto customerDto = new CustomerDto(firstName: firstName, lastName: lastName);
        RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/customers/")
                .request()
                .post(Entity.entity(customerDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Customer.class);
    }

    /*@Test
    public void testLogFileWritten() throws IOException {
        // The log file is using a size and time based policy, which used to silently
        // fail (and not write to a log file). This test ensures not only that the
        // log file exists, but also contains the log line that jetty prints on startup
        final Path log = Paths.get("./logs/application.log");
        assertThat(log).exists();
        final String actual = new String(Files.readAllBytes(log), UTF_8);
        assertThat(actual).contains("0.0.0.0:" + RULE.getLocalPort());
    }*/
}