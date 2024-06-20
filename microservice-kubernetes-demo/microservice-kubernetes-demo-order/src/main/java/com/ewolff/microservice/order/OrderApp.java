package com.ewolff.microservice.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ewolff.microservice")
@EntityScan(basePackages = "com.ewolff.microservice.catalog")
@EnableJpaRepositories(basePackages = "com.ewolff.microservice.catalog")
public class OrderApp {

	public static void main(String[] args) {
		SpringApplication.run(OrderApp.class, args);
	}

}
