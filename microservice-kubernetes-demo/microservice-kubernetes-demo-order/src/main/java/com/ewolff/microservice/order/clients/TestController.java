//package com.ewolff.microservice.order.clients;
//
//import com.ewolff.microservice.order.grpc.client.OrderClient;
//import javax.annotation.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/grpc")
//public class TestController {
//
//  @Resource
//  CatalogClient catalogClient;
//
//  @GetMapping("/test")
//  public String get() {
//    try {
//      System.out.println("gRPC connected");
//      return catalogClient.findAll().toString();
//    } catch (Exception e) {
//      return "Error: " + e.getMessage();
//    }
//  }
//
//}
