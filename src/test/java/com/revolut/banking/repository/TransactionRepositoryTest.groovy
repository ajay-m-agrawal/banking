package com.revolut.banking.repository

import com.revolut.banking.domain.Transaction
import org.dizitart.no2.Nitrite
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.TransactionStatus.COMMITTED
import static com.revolut.banking.domain.TransactionStatus.PENDING
import static com.revolut.banking.domain.TransactionType.DEPOSIT
import static com.revolut.banking.domain.TransactionType.WITHDRAWAL
import static org.hamcrest.core.Is.is
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertThat

@RunWith(MockitoJUnitRunner.class)
class TransactionRepositoryTest {

    Nitrite db
    TransactionRepository repository
    def accountId = "accountId"
    def customerId = "customerId"
    def description = "description"


    @Before
    public void setUp() throws Exception {
        db = Nitrite.builder().openOrCreate()
        repository = new TransactionRepository(db)
    }

    @After
    public void tearDown() throws Exception {
        if(db && !db.isClosed()){
            db.close()
        }
    }

    @Test
    public void testFindAllForStatus() {
        def withdrawalPendingTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId)
                .withCustomer(customerId)
                .withDescription(description)
                .withStatus(PENDING)
                .withType(WITHDRAWAL)
                .withValue(1000)
                .build()

        def depositCommittedTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId)
                .withCustomer(customerId)
                .withDescription(description)
                .withStatus(COMMITTED)
                .withType(DEPOSIT)
                .withValue(1000)
                .build()

        repository.add(withdrawalPendingTransaction)
        repository.add(depositCommittedTransaction)

        def transactions = repository.findAll("customerId", "accountId", PENDING)
        assertThat(transactions.size(), is(1))
        assertEquals(transactions[0].value, new BigDecimal(1000), 0)
    }

    @Test
    void testAddAndFind() {

        String accountId2 = "accountId2"
        String customerId2 = "customerId2"
        def withdrawalSourceTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId)
                .withCustomer(customerId)
                .withDescription(description)
                .withStatus(PENDING)
                .withType(WITHDRAWAL)
                .withValue(new BigDecimal(1000).negate())
                .build()

        def depositDestinationTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId2)
                .withCustomer(customerId2)
                .withDescription(description)
                .withStatus(PENDING)
                .withType(DEPOSIT)
                .withValue(1000)
                .build()

        repository.add(withdrawalSourceTransaction)
        repository.add(depositDestinationTransaction)

        def transaction = repository.find(withdrawalSourceTransaction.id)
        assertEquals(transaction.id, withdrawalSourceTransaction.id)
        assertEquals(transaction.value, new BigDecimal(1000).negate(), 0)

        def transaction2 = repository.find(depositDestinationTransaction.id)
        assertEquals(transaction2.id, depositDestinationTransaction.id)
        assertEquals(transaction2.value, new BigDecimal(1000), 0)

    }

    @Test
    void testFindAll() {

        String accountId2 = "accountId2"
        String customerId2 = "customerId2"
        def withdrawalSourceTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId)
                .withCustomer(customerId)
                .withDescription(description)
                .withStatus(PENDING)
                .withType(WITHDRAWAL)
                .withValue(new BigDecimal(1000).negate())
                .build()

        def depositDestinationTransaction = new Transaction.TransactionBuilder()
                .withAccount(accountId2)
                .withCustomer(customerId2)
                .withDescription(description)
                .withStatus(PENDING)
                .withType(DEPOSIT)
                .withValue(1000)
                .build()

        repository.add(withdrawalSourceTransaction)
        repository.add(depositDestinationTransaction)

        def transactions = repository.findAll(customerId, accountId)
        assertThat(transactions.size(), is(1))
        assertEquals(transactions[0].value, new BigDecimal(1000).negate(), 0)

    }

}
