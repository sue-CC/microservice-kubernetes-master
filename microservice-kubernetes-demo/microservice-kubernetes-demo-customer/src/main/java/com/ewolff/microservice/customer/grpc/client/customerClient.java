package com.ewolff.microservice.customer.grpc.client;

import com.ewolff.microservice.customer.Customer;

import java.util.Collection;

public interface customerClient {
    Customer getCustomer(Long id);
    Collection<Customer> getCustomers();
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    String deleteCustomer(Long id);
}
