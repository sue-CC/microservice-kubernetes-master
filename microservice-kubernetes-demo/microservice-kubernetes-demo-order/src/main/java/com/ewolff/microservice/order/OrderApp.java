package com.ewolff.microservice.order;

import javax.annotation.PostConstruct;
import com.ewolff.microservice.order.logic.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ewolff.microservice.order.logic.Order;

import java.util.Random;

@SpringBootApplication
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

		Random random = new Random();

		for (int i = 0; i < 50; i++) {
			long customerId = random.nextInt(50) + 1;
			Order order = new Order(customerId);
			int numberOfLines = random.nextInt(1) + 1;

			for (int j = 0; j < numberOfLines; j++) {
				int count = random.nextInt(10) + 1;
				long itemId = random.nextInt(50) + 1;
				order.addLine(count, itemId);
				orderRepository.save(order);
			}

		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}
}
