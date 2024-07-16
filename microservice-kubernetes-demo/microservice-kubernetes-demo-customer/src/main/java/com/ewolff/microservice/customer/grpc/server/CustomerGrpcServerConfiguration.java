package com.ewolff.microservice.customer.grpc.server;

import com.ewolff.microservice.customer.CustomerRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class CustomerGrpcServerConfiguration {
        private final Server server;
        private final CustomerRepository customerRepository;

        public CustomerGrpcServerConfiguration(@Value("${customer.server.port:9096}")int port, CustomerRepository customerRepository) {
            this.customerRepository = customerRepository;
            System.out.println("CustomerRepository injected: " + (customerRepository != null));
            ServerBuilder<?> builder = ServerBuilder.forPort(port);
            builder.addService(new CustomerServiceImpl(customerRepository));
            this.server = builder.build();
        }

        @PostConstruct
        public void start() {
            try {
                System.out.println("Attempting to start gRPC server...");
                server.start();
                System.out.println("Customer gRPC Server started, listening on " + server.getPort());
            } catch (Exception e) {
                System.err.println("Failed to start gRPC server: " + e.getMessage());
                throw new IllegalStateException("Not started", e);
            }
        }

        @PreDestroy
        public void stop() {
            System.out.println("Customer gRPC Server stopped");
            server.shutdown();
        }
    }

