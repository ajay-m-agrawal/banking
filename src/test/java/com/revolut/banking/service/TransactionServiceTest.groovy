package com.revolut.banking.service

import com.revolut.banking.domain.Account
import com.revolut.banking.domain.AccountType
import com.revolut.banking.domain.Transaction
import com.revolut.banking.domain.TransactionType
import com.revolut.banking.dto.TransactionDto
import com.revolut.banking.repository.TransactionRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.TransactionStatus.COMMITTED
import static com.revolut.banking.domain.TransactionStatus.PENDING
import static com.revolut.banking.domain.TransactionType.*
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.mockito.Matchers.any
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository mockTransactionRepository
    @Mock
    AccountService mockAccountService
    TransactionService service;
    String customerId = "customerId"
    String accountId = "accountId"
    String transactionId = "transactionId"
    Transaction transaction1
    Transaction transaction2
    String destinationCustomerId = "destinationCustomerId"
    String destinationAccountId = "destinationAccountId"

    @Before
    public void setUp() throws Exception {
        service = new TransactionService(mockTransactionRepository, mockAccountService)
        transaction1 = [
                id : "transaction1",
                type : TRANSFER,
                value : new BigDecimal(1000),
                description : "transfer description",
                accountId : accountId,
                customerId : customerId,
                status : PENDING
        ]

        transaction2 = [
                id : "transaction2",
                type : WITHDRAWAL,
                value : new BigDecimal(1000),
                description : "transfer description",
                accountId : accountId,
                customerId : customerId,
                status : COMMITTED
        ]
    }

    @Test
    void testFindAll() {

        when(mockTransactionRepository.findAll(customerId, accountId)).thenReturn([transaction1, transaction2])
        def transactions = service.findAll(customerId, accountId)

        assertEquals(transactions, [transaction1, transaction2])
        verify(mockTransactionRepository).findAll(customerId, accountId)

    }

    @Test
    void testFindAllForStatus() {

        when(mockTransactionRepository.findAll(customerId, accountId, PENDING)).thenReturn([transaction1])
        def transactions = service.findAll(customerId, accountId, PENDING)

        assertEquals(transactions, [transaction1])
        verify(mockTransactionRepository).findAll(customerId, accountId, PENDING)

    }

    @Test
    void testFind() {

        when(mockTransactionRepository.find(transactionId)).thenReturn(transaction1)
        def transaction = service.find(customerId, accountId, transactionId)

        assertEquals(transaction, transaction1)
        verify(mockTransactionRepository).find(transactionId)
    }

    @Test
    void testProcessWithdrawalTransaction() {

        TransactionDto transactionDto = [
                type: WITHDRAWAL,
                value: new BigDecimal(1000),
                description: "withdrawal request for 1000"
        ]

        Account account = [
                id : accountId,
                customerId : customerId,
                type : AccountType.SAVINGS,
                balance : new BigDecimal(1000),
                availableBalance : new BigDecimal(1000)
        ]
        when(mockAccountService.find(customerId, accountId)).thenReturn(account)
        def transactions = service.process(transactionDto, customerId, accountId)

        assertEquals(transactions.size(), 1)
        assertNotNull(transactions[0].id)
        assertEquals(transactions[0].description, "withdrawal request for 1000")
        assertEquals(transactions[0].value, new BigDecimal(1000).negate(), 0)
        assertEquals(transactions[0].type, WITHDRAWAL)
        assertEquals(transactions[0].accountId, accountId)
        assertEquals(transactions[0].customerId, customerId)
        assertEquals(transactions[0].status, PENDING)

        verify(mockTransactionRepository).add(any(Transaction.class))
    }

    @Test
    void testProcessDepositTransaction() {

        TransactionDto transactionDto = [
                type: DEPOSIT,
                value: new BigDecimal(1000),
                description: "deposit request for 1000"
        ]

        Account account = [
                id : accountId,
                customerId : customerId,
                type : AccountType.SAVINGS,
                balance : new BigDecimal(1000),
                availableBalance : new BigDecimal(1000)
        ]
        when(mockAccountService.find(customerId, accountId)).thenReturn(account)

        def transactions = service.process(transactionDto, customerId, accountId)

        assertEquals(transactions.size(), 1)
        assertNotNull(transactions[0].id)
        assertEquals(transactions[0].description, "deposit request for 1000")
        assertEquals(transactions[0].value, new BigDecimal(1000), 0)
        assertEquals(transactions[0].type, DEPOSIT)
        assertEquals(transactions[0].accountId, accountId)
        assertEquals(transactions[0].customerId, customerId)
        assertEquals(transactions[0].status, PENDING)
    }

    @Test
    void testProcessTransferTransaction() {

        TransactionDto transactionDto = [
                type: TRANSFER,
                value: new BigDecimal(1000),
                description: "transfer request for 1000",
                destinationAccountId: destinationAccountId,
                destinationCustomerId: destinationCustomerId
        ]

        Account sourceAccount = [
                id : accountId,
                customerId : customerId,
                type : AccountType.SAVINGS,
                balance : new BigDecimal(1000),
                availableBalance : new BigDecimal(1000)
        ]
        when(mockAccountService.find(customerId, accountId)).thenReturn(sourceAccount)
        Account destinationAccount = [
                id : destinationAccountId,
                customerId : destinationCustomerId,
                type : AccountType.SAVINGS,
                balance : new BigDecimal(1000),
                availableBalance : new BigDecimal(1000)
        ]
        when(mockAccountService.find(destinationCustomerId, destinationAccountId)).thenReturn(destinationAccount)

        def transactions = service.process(transactionDto, customerId, accountId)

        assertEquals(transactions.size(), 2)
        assertNotNull(transactions[0].id)
        assertEquals(transactions[0].description, "transfer request for 1000")
        assertEquals(transactions[0].value, new BigDecimal(1000).negate(), 0)
        assertEquals(transactions[0].type, TRANSFER)
        assertEquals(transactions[0].accountId, accountId)
        assertEquals(transactions[0].customerId, customerId)
        assertEquals(transactions[0].status, PENDING)

        assertNotNull(transactions[1].id)
        assertEquals(transactions[1].description, "transfer request for 1000")
        assertEquals(transactions[1].value, new BigDecimal(1000), 0)
        assertEquals(transactions[1].type, DEPOSIT)
        assertEquals(transactions[1].accountId, destinationAccountId)
        assertEquals(transactions[1].customerId, destinationCustomerId)
        assertEquals(transactions[1].status, PENDING)
    }
}
