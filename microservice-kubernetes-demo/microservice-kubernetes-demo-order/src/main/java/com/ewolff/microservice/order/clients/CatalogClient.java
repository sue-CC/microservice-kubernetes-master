package com.ewolff.microservice.order.clients;


import org.springframework.context.annotation.Bean;

import java.util.Collection;

public interface CatalogClient {
	Item getOne(Long itemId);

	Collection<Item> findAll();

	double price(Long itemId);
}