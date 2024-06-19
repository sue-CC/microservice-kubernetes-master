package com.ewolff.microservice.order.grpc;

import com.ewolff.microservice.order.grpc.client.OrderClient;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grpc")
public class TestController {

  @Resource
  OrderClient orderClient;

  @GetMapping("/test")
  public String get() {
    try {
      return orderClient.getOrderList();
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

}
