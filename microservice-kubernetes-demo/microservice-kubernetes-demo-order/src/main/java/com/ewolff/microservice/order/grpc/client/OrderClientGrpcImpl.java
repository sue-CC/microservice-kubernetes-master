package com.ewolff.microservice.order.grpc.client;

import com.ewolff.microservice.order.grpc.GetOrderListResponse;
import com.ewolff.microservice.order.grpc.OrderServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderClientGrpcImpl implements OrderClient {

  private final OrderServiceGrpc.OrderServiceBlockingStub stub;

  public OrderClientGrpcImpl(@Value("${grpc.client.order:127.0.0.1:9091}") String url) {
    ManagedChannel channel = ManagedChannelBuilder.forTarget(url)
        .usePlaintext().build();
    this.stub = OrderServiceGrpc.newBlockingStub(channel);
  }


  @Override
  public String getOrderList() {
    GetOrderListResponse orderList = stub.getOrderList(Empty.getDefaultInstance());
    return orderList.getOrderlistHtml();
  }
}
