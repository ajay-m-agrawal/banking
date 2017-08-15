package com.revolut.banking.repository

import com.revolut.banking.domain.Customer
import com.revolut.banking.dto.CustomerDto
import org.dizitart.no2.Nitrite
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import static org.junit.Assert.assertEquals

@RunWith(MockitoJUnitRunner.class)
class CustomerRepositoryTest {

    Nitrite db
    CustomerRepository repository

    @Before
    void setUp() {
        db = Nitrite.builder().openOrCreate()
        repository = new CustomerRepository(db)
    }

    @After
    void tearDown() {
        if(db && !db.isClosed()){
            db.close()
        }
    }

    @Test
    void testAddAndFind() {

        CustomerDto customerDto = [
                firstName: "first",
                lastName : "last"
        ]
        Customer customer1 = Customer.newCustomer(customerDto)
        Customer customer2 = Customer.newCustomer(customerDto)
        repository.add(customer1)
        repository.add(customer2)
        Customer customer = repository.find(customer1.id);

        assertEquals(customer, customer1)
    }

    @Test
    void testFindAll() {

        CustomerDto customerDto = [
                firstName: "first",
                lastName : "last"
        ]
        Customer customer1 = Customer.newCustomer(customerDto)
        Customer customer2 = Customer.newCustomer(customerDto)
        repository.add(customer1)
        repository.add(customer2)
        List<Customer> customers = repository.findAll();

        assertEquals(customers, [customer1, customer2])

    }


}
