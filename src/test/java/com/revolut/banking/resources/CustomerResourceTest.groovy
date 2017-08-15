package com.revolut.banking.resources

import com.revolut.banking.domain.Customer
import com.revolut.banking.dto.CustomerDto
import com.revolut.banking.service.CustomerService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class CustomerResourceTest {

    @Mock
    CustomerService mockCustomerService
    CustomerResource customerResource
    String customerId = "customerId"


    @Before
    public void setUp() throws Exception {
        customerResource = new CustomerResource(mockCustomerService)
    }

    @Test
    void testGetCustomers() {

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

        when(mockCustomerService.findAll()).thenReturn([customer1, customer2])
        List<Customer> customers = customerResource.getCustomers()

        assertEquals(customers, [customer1, customer2])
        verify(mockCustomerService).findAll()
    }

    @Test
    void testGetCustomer() {
        Customer customer1 = [
                id : "customerId",
                firstName : "first",
                lastName : "last"
        ]

        when(mockCustomerService.find(customerId)).thenReturn(customer1)
        Customer customer = customerResource.getCustomer(customerId)

        assertEquals(customer, customer1)
        verify(mockCustomerService).find(customerId)
    }

    @Test
    void testCreateCustomer() {

        def customerDto = new CustomerDto()
        customerResource.createCustomer(customerDto)

        verify(mockCustomerService).create(customerDto)
    }
}
