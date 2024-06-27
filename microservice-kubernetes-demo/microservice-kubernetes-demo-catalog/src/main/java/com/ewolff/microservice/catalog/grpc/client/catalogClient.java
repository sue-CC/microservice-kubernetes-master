package com.ewolff.microservice.catalog.grpc.client;

import com.ewolff.microservice.catalog.Item;

import java.util.Collection;

public interface catalogClient {
    Item getItem(Long id);
    Collection<Item> getItems();
    Item addItem(Item item);
    Item updateItem(Long id, String name, double price);
    String deleteItem(Long id);
    Collection<Item> searchItems(String query);
}
