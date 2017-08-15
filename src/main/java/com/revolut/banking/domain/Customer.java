package com.revolut.banking.domain;

import com.revolut.banking.dto.CustomerDto;

import static java.util.UUID.randomUUID;

public class Customer {

    private String id;
    private String firstName;
    private String lastName;

    public static Customer newCustomer(CustomerDto customerDto) {
        return new CustomerBuilder().withId(randomUUID().toString()).withFirstName(customerDto.firstName).withLastName(customerDto.lastName).build();
    }


    public Customer() {
    }

    private Customer(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public static class CustomerBuilder {

        private String id;
        private String firstName;
        private String lastName;

        private CustomerBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Customer build() {
            if(id == null){
                id = randomUUID().toString();
            }
            return new Customer(id, firstName, lastName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!id.equals(customer.id)) return false;
        if (!firstName.equals(customer.firstName)) return false;
        return lastName.equals(customer.lastName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}