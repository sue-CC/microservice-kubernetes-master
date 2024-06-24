package com.ewolff.microservice.order.clients;

import com.ewolff.microservice.customer.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.protobuf.Empty;


import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class CustomerGrpcClientImpl implements CustomerClient {
    private static final Logger logger = Logger.getLogger(CustomerGrpcClientImpl.class.getName());
    private final CustomerServiceGrpc.CustomerServiceBlockingStub customerService;

    // construct client for accessing catalog server using the channel
    public CustomerGrpcClientImpl(@Value("${customer.service.host:localhost}") String host,
                                @Value("${customer.service.port:9092}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.customerService = CustomerServiceGrpc.newBlockingStub(channel);
    }

    // get information from the channel
    @Override
    public boolean isValidCustomerId(long customerId) {
        CustomerProto.CustomerRequest request = CustomerProto.CustomerRequest.newBuilder().setCustomerId(customerId).build();
        CustomerProto.CustomerResponse response = customerService.getCustomer(request);
        return null != response;
    }


    @Override
    public Customer getOne(long customerId) {
        CustomerProto.CustomerRequest request = CustomerProto.CustomerRequest.newBuilder().setCustomerId(customerId).build();
        CustomerProto.CustomerResponse response = customerService.getCustomer(request);
        System.out.println("Received a customer through gRPC.");
        return new Customer(response.getCustomerId(),
                response.getFirstname(), response.getName(), response.getEmail(), response.getStreet(), response.getCity()
        );

    }

    @Override
    public Collection<Customer> findAll() {
        Empty request = Empty.newBuilder().build();
        CustomerProto.CustomerListResponse response = customerService.getCustomerList(request);
        System.out.println("Received customers through gRPC.");
        return response.getCustomersList().stream()
                .map(customer -> new Customer(customer.getCustomerId(),
                        customer.getFirstname(), customer.getName(), customer.getEmail(), customer.getStreet(), customer.getCity()))
                .collect(Collectors.toList());
    }
}