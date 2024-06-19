package com.ewolff.microservice.order.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerConfig {

  private final Server server;

  public GrpcServerConfig(@Value("${grpc.server.port:9091}") int port) {
    ServerBuilder<?> builder = ServerBuilder.forPort(port);
    builder.addService(new OrderGrpcService());
    builder.addService((new CatalogGrpcService()));
    builder.addService(new CustomerGrpcService());
    this.server = builder.build();
  }

  @PostConstruct
  public void start() throws Exception {
    server.start();
  }

  @PreDestroy
  public void stop() {
    server.shutdown();
  }
}
