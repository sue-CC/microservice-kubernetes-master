package com.ewolff.microservice.catalog.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CatalogGrpcServerConfiguration {

    @Autowired
    private CatalogServiceImpl catalogService;

    @Bean
    public Server grpcServer() throws IOException {
        // bind the port
        Server server = ServerBuilder.forPort(9091)
                .addService(catalogService) // publish service
                .build(); // create server object
        server.start();
        System.out.println("the listitems:" + server.getServices());
        System.out.println("Catalog gRPC Server started, listening on " + server.getPort());
        return server;
    }
}
