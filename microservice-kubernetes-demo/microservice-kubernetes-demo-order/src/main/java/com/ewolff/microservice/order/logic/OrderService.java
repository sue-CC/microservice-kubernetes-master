package com.ewolff.microservice.order.logic;

import com.ewolff.microservice.order.clients.CatalogGrpcClientImp;
import com.ewolff.microservice.order.clients.CustomerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.customer.grpc.*;

@Service
class OrderService {

	private OrderRepository orderRepository;
	private CustomerClient customerClient;
	private CatalogGrpcClientImp itemClient;

	@Autowired
	private OrderService(OrderRepository orderRepository,
						 CustomerClient customerClient, CatalogGrpcClientImp itemClient) {
		super();
		this.orderRepository = orderRepository;
		this.customerClient = customerClient;
		this.itemClient = itemClient;
	}

	public Order order(Order order) {
		if (order.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		if (!customerClient.isValidCustomerId(order.getCustomerId())) {
			throw new IllegalArgumentException("Customer does not exist!");
		}
		return orderRepository.save(order);
	}

	public double getPrice(long orderId) {
		return orderRepository.findById(orderId).get().totalPrice(itemClient);
	}

}
