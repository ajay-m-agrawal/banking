package com.revolut.banking.repository

import com.revolut.banking.domain.Account
import org.dizitart.no2.Nitrite
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.AccountType.SAVINGS
import static org.junit.Assert.assertEquals

@RunWith(MockitoJUnitRunner.class)
class AccountRepositoryTest {
    Nitrite db
    AccountRepository repository
    String accountId = "accountId"
    String customerId = "customerId"

    @Before
    public void setUp() throws Exception {
        db = Nitrite.builder().openOrCreate()
        repository = new AccountRepository(db)
    }

    @After
    public void tearDown() throws Exception {
        if(db && !db.isClosed()){
            db.close()
        }
    }

    @Test
    void testAddAndFind() {

        def account1 = new Account.AccountBuilder()
                .withId(accountId)
                .withType(SAVINGS)
                .withCustomerId(customerId)
                .withBalance(0)
                .build()
        repository.add(account1)

        def account = repository.find(account1.id)

        assertEquals(account, account1)

    }

    @Test
    void testFindAll() {
        def account1 = new Account.AccountBuilder()
                .withType(SAVINGS)
                .withCustomerId(customerId)
                .withBalance(0)
                .build()
        repository.add(account1)

        def account2 = new Account.AccountBuilder()
                .withType(SAVINGS)
                .withCustomerId(customerId)
                .withBalance(0)
                .build()
        repository.add(account2)

        def account3 = new Account.AccountBuilder()
                .withType(SAVINGS)
                .withCustomerId("customerId2")
                .withBalance(0)
                .build()
        repository.add(account3)

        def accounts = repository.findAll(customerId)

        assertEquals(accounts, [account1, account2])
    }
}
