package com.ewolff.microservice.order.clients;

import java.util.Collection;

public interface CustomerClient{
	Customer getOne(long customerId);

	Collection<Customer> findAll();

	boolean isValidCustomerId(long customerId);
}