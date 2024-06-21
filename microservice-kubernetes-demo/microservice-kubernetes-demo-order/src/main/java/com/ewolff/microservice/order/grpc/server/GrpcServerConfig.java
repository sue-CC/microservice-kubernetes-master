//package com.ewolff.microservice.order.grpc.server;
//
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//@Component
//public class GrpcServerConfig {
//
//  private final Server server;
//  private final OrderGrpcService orderGrpcService;
//  private final CatalogGrpcService catalogGrpcService;
//  private final CustomerGrpcService customerGrpcService;
//
//  @Autowired
//  public GrpcServerConfig(@Value("${grpc.server.port:9091}") int port,
//                          OrderGrpcService orderGrpcService,
//                          CatalogGrpcService catalogGrpcService,
//                          CustomerGrpcService customerGrpcService) {
//
//    this.orderGrpcService = orderGrpcService;
//    this.catalogGrpcService = catalogGrpcService;
//    this.customerGrpcService = customerGrpcService;
//
//    ServerBuilder<?> builder = ServerBuilder.forPort(port)
//            .addService(this.orderGrpcService)
//            .addService(this.catalogGrpcService)
//            .addService(this.customerGrpcService);
//
//    this.server = builder.build();
//  }
//
//  @PostConstruct
//  public void start() throws Exception {
//    server.start();
//    System.out.println("gRPC Server started, listening on port " + server.getPort());
//    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//      System.err.println("*** shutting down gRPC server since JVM is shutting down");
//      GrpcServerConfig.this.stop();
//      System.err.println("*** server shut down");
//    }));
//  }
//
//  @PreDestroy
//  public void stop() {
//    if (server != null) {
//      server.shutdown();
//    }
//  }
//}
