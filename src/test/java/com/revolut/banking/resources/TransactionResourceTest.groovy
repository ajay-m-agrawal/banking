package com.revolut.banking.resources

import com.revolut.banking.domain.Transaction
import com.revolut.banking.dto.TransactionDto
import com.revolut.banking.service.TransactionService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.TransactionStatus.PENDING
import static com.revolut.banking.domain.TransactionType.DEPOSIT
import static com.revolut.banking.domain.TransactionType.TRANSFER
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class TransactionResourceTest {

    TransactionResource resource
    @Mock
    TransactionService mockTransactionService
    String customerId = "customerId"
    String accountId = "accountId"
    String transactionId = "transactionId"
    Transaction stubbedTransaction1 = [
            id : "transactionId1",
            type : TRANSFER,
            value : new BigDecimal(1000),
            description : "transfer description",
            accountId : accountId,
            customerId : customerId,
            status : PENDING
    ]

    Transaction stubbedTransaction2 = [
            id : "transactionId2",
            type : DEPOSIT,
            value : new BigDecimal(1000),
            description : "deposit description",
            accountId : accountId,
            customerId : customerId,
            status : PENDING
    ]

    @Before
    void setUp() {

        resource = new TransactionResource(mockTransactionService);
    }

    @Test
    void testGetTransactions() {

        //When
        when(mockTransactionService.find(customerId, accountId, transactionId)).thenReturn(stubbedTransaction1)
        Transaction actualTransaction = resource.getTransaction(customerId, accountId, transactionId)

        //Then
        assertEquals(actualTransaction, stubbedTransaction1)
        verify(mockTransactionService).find(customerId, accountId, transactionId)
    }

    @Test
    void testGetTransaction() {

        //When
        when(mockTransactionService.findAll(customerId, accountId)).thenReturn([stubbedTransaction1, stubbedTransaction2])
        List<Transaction> actualTransactions = resource.getTransactions(customerId, accountId)

        //Then
        assertEquals(actualTransactions, [stubbedTransaction1, stubbedTransaction2])
        verify(mockTransactionService).findAll(customerId, accountId)
    }

    @Test
    void testCreateTransaction() {

        TransactionDto transactionDto = [
                type: DEPOSIT,
                value: new BigDecimal(1500),
                description: "deposit description"
        ]

        when(mockTransactionService.process(transactionDto, customerId, accountId)).thenReturn([stubbedTransaction2])
        def actualTransactions = resource.createTransaction(customerId, accountId, transactionDto)

        assertEquals(actualTransactions, [stubbedTransaction2])
        verify(mockTransactionService).process(transactionDto, customerId, accountId)
    }
}
