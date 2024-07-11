package com.ewolff.microservice.order.grpc.server;

import com.ewolff.microservice.order.logic.OrderRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class OrderGrpcServerConfiguration {
    private final Server server;
    private final OrderRepository orderRepository;

    public OrderGrpcServerConfiguration(@Value("${order.server.port:9092}")int port, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        System.out.println("OrderRepository injected: " + (orderRepository != null));
        ServerBuilder<?> builder = ServerBuilder.forPort(port);
        builder.addService(new OrderServiceImpl(orderRepository));
        this.server = builder.build();
    }

    @PostConstruct
    public void start() {
        try {
            System.out.println("Attempting to start gRPC server...");
            server.start();
            System.out.println("Order gRPC Server started, listening on " + server.getPort());
        } catch (Exception e) {
            System.err.println("Failed to start gRPC server: " + e.getMessage());
            throw new IllegalStateException("Not started", e);
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Order gRPC Server stopped");
        server.shutdown();
    }
}

