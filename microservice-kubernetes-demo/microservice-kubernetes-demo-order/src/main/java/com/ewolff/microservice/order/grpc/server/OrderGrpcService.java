//package com.ewolff.microservice.order.grpc.server;
//
//import com.ewolff.microservice.order.grpc.AddLineRequest;
//import com.ewolff.microservice.order.grpc.AddLineResponse;
//import com.ewolff.microservice.order.grpc.CreateOrderRequest;
//import com.ewolff.microservice.order.grpc.CreateOrderResponse;
//import com.ewolff.microservice.order.grpc.DeleteOrderResponse;
//import com.ewolff.microservice.order.grpc.GetOrderListResponse;
//import com.ewolff.microservice.order.grpc.GetOrderResponse;
//import com.ewolff.microservice.order.grpc.OrderId;
//import com.ewolff.microservice.order.grpc.OrderServiceGrpc.OrderServiceImplBase;
//import com.google.protobuf.Empty;
//import io.grpc.stub.StreamObserver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OrderGrpcService extends OrderServiceImplBase {
//
//  private static final Logger log = LoggerFactory.getLogger(OrderGrpcService.class);
//
//  @Override
//  public void getOrderList(Empty request, StreamObserver<GetOrderListResponse> responseObserver) {
//    log.info("getOrderList:{}", request);
//    GetOrderListResponse reply = GetOrderListResponse.newBuilder().setOrderlistHtml("test").build();
//    responseObserver.onNext(reply);
//    responseObserver.onCompleted();
//  }
//
//  @Override
//  public void getOrder(OrderId request, StreamObserver<GetOrderResponse> responseObserver) {
//    super.getOrder(request, responseObserver);
//    log.info("getOrder:{}", request);
//
//  }
//
//  @Override
//  public void createOrder(CreateOrderRequest request,
//      StreamObserver<CreateOrderResponse> responseObserver) {
//    super.createOrder(request, responseObserver);
//  }
//
//  @Override
//  public void addLine(AddLineRequest request, StreamObserver<AddLineResponse> responseObserver) {
//    super.addLine(request, responseObserver);
//  }
//
//  @Override
//  public void deleteOrder(OrderId request, StreamObserver<DeleteOrderResponse> responseObserver) {
//    super.deleteOrder(request, responseObserver);
//  }
//}
