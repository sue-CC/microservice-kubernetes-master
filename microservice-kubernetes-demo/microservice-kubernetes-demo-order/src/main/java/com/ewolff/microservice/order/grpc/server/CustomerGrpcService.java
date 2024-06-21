//package com.ewolff.microservice.order.grpc.server;
//
//import com.ewolff.microservice.order.grpc.CustomerList;
//import com.ewolff.microservice.order.grpc.CustomerServiceGrpc;
//import com.google.protobuf.Empty;
//import io.grpc.stub.StreamObserver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomerGrpcService extends CustomerServiceGrpc.CustomerServiceImplBase {
//    private static final Logger log = LoggerFactory.getLogger(CustomerGrpcService.class);
//
//    @Override
//    public void getAllCustomers(Empty request, StreamObserver<CustomerList> responseObserver) {
//        super.getAllCustomers(request, responseObserver);
//        // new encapsulated Response
//        CustomerList customerList = CustomerList.newBuilder().build();
//        // response to client side (order service)
//        responseObserver.onNext(customerList);
//        responseObserver.onCompleted();
//    }
//}
