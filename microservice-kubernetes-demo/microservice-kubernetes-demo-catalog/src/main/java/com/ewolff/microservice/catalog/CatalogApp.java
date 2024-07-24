package com.ewolff.microservice.catalog;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Random;

@ComponentScan
@EnableAutoConfiguration
@Component
public class CatalogApp {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogApp(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@PostConstruct
	public void generateTestData() {
		// Add the original entries
		itemRepository.save(new Item("iPod", 42.0));
		itemRepository.save(new Item("iPod touch", 21.0));
		itemRepository.save(new Item("iPod nano", 1.0));
		itemRepository.save(new Item("Apple TV", 100.0));

		// Arrays with sample data
		String[] itemNames = {"Laptop", "Smartphone", "Tablet", "Headphones", "Smartwatch", "Camera", "Monitor", "Keyboard", "Mouse", "Printer"};
		double[] prices = {199.99, 299.99, 399.99, 49.99, 149.99, 499.99, 99.99, 29.99, 19.99, 89.99};

		Random random = new Random();

		// Generate 50 random entries
		for (int i = 0; i < 50; i++) {
			String itemName = itemNames[random.nextInt(itemNames.length)];
			double price = prices[random.nextInt(prices.length)];
			itemRepository.save(new Item(itemName, price));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogApp.class, args);
	}
}
