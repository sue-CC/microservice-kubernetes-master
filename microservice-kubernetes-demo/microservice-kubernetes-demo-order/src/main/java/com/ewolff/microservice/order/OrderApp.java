package com.ewolff.microservice.order;

import com.ewolff.microservice.order.logic.Order;
import com.ewolff.microservice.order.logic.OrderRepository;
import jdk.jfr.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

@SpringBootApplication
//@EntityScan(basePackages = {"com.ewolff.microservice.catalog", "com.ewolff.microservice.order"})
//@EnableJpaRepositories(basePackages = {"com.ewolff.microservice.catalog","com.ewolff.microservice.catalog"})
public class OrderApp {
	private final OrderRepository orderRepository;


	@Autowired
	public OrderApp(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@PostConstruct
	public void generateTestData() {
		Order order1 = new Order(1);
		order1.addLine(2, 1); // Adding two items with itemId 101
		order1.addLine(1, 2); // Adding one item with itemId 102

		Order order2 = new Order(2);
		order2.addLine(3, 3); // Adding three items with itemId 103

		orderRepository.save(order1);
		orderRepository.save(order2);
	}
	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}

}
