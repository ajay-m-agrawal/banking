package com.revolut.banking.service

import com.revolut.banking.domain.Customer
import com.revolut.banking.repository.CustomerRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class CustomerServiceTest {

    CustomerService customerService
    @Mock
    CustomerRepository mockCustomerRepository
    def customerId = "customerId"

    @Before
    public void setUp() throws Exception {
        customerService = new CustomerService(mockCustomerRepository)
    }

    @Test
    void testFind() {
        Customer customer1 = [
                id : "customerId",
                firstName : "first",
                lastName : "last"
        ]
        when(mockCustomerRepository.find(customerId)).thenReturn(customer1)
        Customer customer = customerService.find(customerId)

        assertEquals(customer, customer1)
        verify(mockCustomerRepository).find(customerId)
    }

    @Test
    void testCreate() {
    }

    @Test
    void testFindAll() {
        Customer customer1 = [
                id : "customerId",
                firstName : "first",
                lastName : "last"
        ]

        Customer customer2 = [
                id : "customerId2",
                firstName : "first",
                lastName : "last"
        ]

        when(mockCustomerRepository.findAll()).thenReturn([customer1, customer2])
        List<Customer> customers = customerService.findAll()

        assertEquals(customers, [customer1, customer2])
        verify(mockCustomerRepository).findAll()
    }
}
