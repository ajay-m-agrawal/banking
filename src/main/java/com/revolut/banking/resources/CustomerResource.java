package com.revolut.banking.resources;

import com.codahale.metrics.annotation.Timed;
import com.revolut.banking.domain.Customer;
import com.revolut.banking.dto.CustomerDto;
import com.revolut.banking.service.CustomerService;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }


    @GET
    @Path("{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Customer getCustomer(@PathParam("customerId") @NotEmpty String customerId) {
        return customerService.find(customerId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public Customer createCustomer(@Valid CustomerDto customerDto) {
        return customerService.create(customerDto);
    }
}
