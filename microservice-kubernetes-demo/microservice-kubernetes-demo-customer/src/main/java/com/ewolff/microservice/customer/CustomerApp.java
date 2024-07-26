package com.ewolff.microservice.customer;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@ComponentScan
@EnableAutoConfiguration
@Component
public class CustomerApp {

	private final CustomerRepository customerRepository;

	@Autowired
	public CustomerApp(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@PostConstruct
	public void generateTestData() {
		// Add the two original entries
		customerRepository
				.save(new Customer("Eberhard", "Wolff", "eberhard.wolff@gmail.com", "Unter den Linden", "Berlin"));
		customerRepository.save(new Customer("Rod", "Johnson", "rod@somewhere.com", "Market Street", "San Francisco"));

		// Arrays with sample data
		String[] firstNames = {"John", "Jane", "Alex", "Emily", "Chris", "Katie", "Michael", "Sarah", "David", "Laura"};
		String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};
		String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
		String[] streets = {"Main St", "High St", "Broadway", "Elm St", "Oak St", "Maple St", "Cedar St", "Pine St", "Walnut St", "Peach St"};

		Random random = new Random();

		// Generate 50 random entries
		for (int i = 0; i < 1000; i++) {
			String firstName = firstNames[random.nextInt(firstNames.length)];
			String lastName = lastNames[random.nextInt(lastNames.length)];
			String email = generateEmail(firstName, lastName);
			String street = streets[random.nextInt(streets.length)];
			String city = cities[random.nextInt(cities.length)];
			customerRepository.save(new Customer(firstName, lastName, email, street, city));
		}
	}

	private String generateEmail(String firstName, String lastName) {
		return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + UUID.randomUUID().toString().substring(0, 8) + ".com";
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerApp.class, args);
	}

}