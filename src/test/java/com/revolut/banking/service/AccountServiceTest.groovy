package com.revolut.banking.service

import com.revolut.banking.domain.Account
import com.revolut.banking.domain.Transaction
import com.revolut.banking.domain.TransactionType
import com.revolut.banking.dto.AccountDto
import com.revolut.banking.repository.AccountRepository
import com.revolut.banking.repository.TransactionRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.AccountType.SAVINGS
import static com.revolut.banking.domain.TransactionStatus.PENDING
import static org.mockito.Matchers.any
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class AccountServiceTest extends GroovyTestCase {

    @Mock
    AccountRepository mockAccountRepository
    @Mock
    TransactionRepository mockTransactionRepository
    AccountService accountService;
    String accountId = "accountId"
    String customerId = "customerId"

    @Before
    public void setup(){
        accountService = new AccountService(mockAccountRepository, mockTransactionRepository)
    }

    @Test
    void testFindAll() {


        def account1 = new Account.AccountBuilder()
                .withId(accountId)
                .withType(SAVINGS)
                .withCustomerId(customerId)
                .withBalance(0)
                .build()
        def transaction1 = new Transaction.TransactionBuilder()
                .withType(TransactionType.DEPOSIT)
                .withStatus(PENDING)
                .withCustomer(customerId)
                .withAccount(accountId)
                .withValue(new BigDecimal(500).negate())
                .build()

        def transaction2 = new Transaction.TransactionBuilder()
                .withType(TransactionType.DEPOSIT)
                .withStatus(PENDING)
                .withCustomer(customerId)
                .withAccount(accountId)
                .withValue(2000)
                .build()

        when(mockTransactionRepository.findAll(customerId, accountId, PENDING)).thenReturn([transaction1, transaction2])
        when(mockAccountRepository.findAll(customerId)).thenReturn([account1])

        List<Account> accounts = accountService.findAll(customerId)
        assertEquals(accounts.size(), 1)
        assertEquals(accounts[0].id, account1.id)
        assertEquals(accounts[0].availableBalance, 1500, 0)
        verify(mockAccountRepository).findAll(customerId)
        verify(mockTransactionRepository).findAll(customerId, accountId, PENDING)

    }

    @Test
    void testFind() {


        def account1 = new Account.AccountBuilder()
                .withId(accountId)
                .withType(SAVINGS)
                .withCustomerId(customerId)
                .withBalance(0)
                .build()
        def transaction1 = new Transaction.TransactionBuilder()
                .withType(TransactionType.DEPOSIT)
                .withStatus(PENDING)
                .withCustomer(customerId)
                .withAccount(accountId)
                .withValue(new BigDecimal(500).negate())
                .build()

        def transaction2 = new Transaction.TransactionBuilder()
                .withType(TransactionType.DEPOSIT)
                .withStatus(PENDING)
                .withCustomer(customerId)
                .withAccount(accountId)
                .withValue(2000)
                .build()

        when(mockTransactionRepository.findAll(customerId, accountId, PENDING)).thenReturn([transaction1, transaction2])
        when(mockAccountRepository.find(accountId)).thenReturn(account1)

        Account account = accountService.find(customerId, accountId)
        assertEquals(account.id, account1.id)
        assertEquals(account.availableBalance, 1500, 0)
        verify(mockAccountRepository).find(accountId)
        verify(mockTransactionRepository).findAll(customerId, accountId, PENDING)
    }

    @Test
    void testCreate() {
        AccountDto accountDto = [
                type : SAVINGS
        ]

        Account account = accountService.create(customerId, accountDto)

        assertNotNull(account.id)
        assertEquals(account.type, SAVINGS)
        assertEquals(account.customerId, customerId)
        assertEquals(account.balance, new BigDecimal(0))
        assertEquals(account.availableBalance, new BigDecimal(0))
        verify(mockAccountRepository).add(any(Account.class))

    }
}
