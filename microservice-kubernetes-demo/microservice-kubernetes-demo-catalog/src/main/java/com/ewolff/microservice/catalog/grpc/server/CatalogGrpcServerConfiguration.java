package com.ewolff.microservice.catalog.grpc.server;

import com.ewolff.microservice.catalog.ItemRepository;
import com.ewolff.microservice.catalog.grpc.server.CatalogServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
public class CatalogGrpcServerConfiguration {

    private final Server server;
    private final ItemRepository itemRepository;


    public CatalogGrpcServerConfiguration(@Value("${grpc.server.port:9091}")int port, ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        System.out.println("ItemRepository injected: " + (itemRepository != null));
        ServerBuilder<?> builder = ServerBuilder.forPort(port);
        builder.addService(new CatalogServiceImpl(itemRepository));
        this.server = builder.build();
    }

    @PostConstruct
    public void start() {
        try {
            System.out.println("Attempting to start gRPC server...");
            server.start();
            System.out.println("Catalog gRPC Server started, listening on " + server.getPort());
        } catch (Exception e) {
            System.err.println("Failed to start gRPC server: " + e.getMessage());
            throw new IllegalStateException("Not started", e);
        }
    }

    @PreDestroy
    public void stop() {
        System.out.println("Catalog gRPC Server stopped");
        server.shutdown();
    }
}
