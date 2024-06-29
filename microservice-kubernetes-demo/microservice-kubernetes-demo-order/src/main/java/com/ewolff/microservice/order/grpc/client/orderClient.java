package com.ewolff.microservice.order.grpc.client;

import com.ewolff.microservice.order.logic.Order;

import java.util.Collection;

public interface orderClient {
    Order getOrder(long orderId);
    Collection<Order> getOrders();
    Order createOrder(Order order);
    String deleteOrder(long orderId);
}
