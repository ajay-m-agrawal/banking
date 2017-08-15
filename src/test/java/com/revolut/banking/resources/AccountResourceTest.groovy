package com.revolut.banking.resources

import com.revolut.banking.domain.Account
import com.revolut.banking.dto.AccountDto
import com.revolut.banking.service.AccountService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static com.revolut.banking.domain.AccountType.SAVINGS
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class AccountResourceTest {

    AccountResource resource
    @Mock
    AccountService mockAccountService
    String customerId = "customerId"
    String accountId = "accountId"

    @Before
    public void setUp() throws Exception {
        resource = new AccountResource(mockAccountService)
    }

    @Test
    void testGetAccounts() {

        Account account1 = [
            id : "account1"
        ]
        Account account2 = [
            id : "account2"
        ]
        when(mockAccountService.findAll(customerId)).thenReturn([account1, account2])
        def accounts = resource.getAccounts(customerId)

        assertEquals(accounts, [account1, account2])
        verify(mockAccountService).findAll(customerId)
    }

    @Test
    void testGetAccount() {

        Account account1 = [
                id : "account1"
        ]

        when(mockAccountService.find(customerId, accountId)).thenReturn(account1)
        def account = resource.getAccount(customerId, accountId)

        assertEquals(account, account1)
        verify(mockAccountService).find(customerId, accountId)

    }

    @Test
    void testCreateAccount() {

        AccountDto accountDto = [
                type : SAVINGS
        ]
        Account mockAccount = [
                id : "accountId"
        ]
        when(mockAccountService.create(customerId, accountDto)).thenReturn(mockAccount)
        Account account = resource.createAccount(customerId, accountDto)

        assertEquals(account, mockAccount)
        verify(mockAccountService).create(customerId, accountDto)
    }
}
