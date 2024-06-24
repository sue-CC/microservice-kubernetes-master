package com.ewolff.microservice.customer.grpc.server;

import com.ewolff.microservice.customer.Customer;
import com.ewolff.microservice.customer.CustomerRepository;
import com.ewolff.microservice.customer.grpc.CustomerProto;
import com.ewolff.microservice.customer.grpc.CustomerServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void getCustomer(CustomerProto.CustomerRequest request, StreamObserver<CustomerProto.CustomerResponse> responseObserver) {
        logger.info("Received getCustomer gRPC request for itemId: {}", request.getCustomerId());
        // get the customerId through request
        long customerId = request.getCustomerId();

        // get the item information through itemId
        Customer customer = findCustomerById(customerId);

        // construct a new builder to encapsulate for response
        // inject related information into the builder
        if (customer != null) {
            CustomerProto.CustomerResponse response = CustomerProto.CustomerResponse.newBuilder()
                .setCustomerId(customer.getId())
                .setName(customer.getName())
                .setFirstname(customer.getFirstname())
                .setEmail(customer.getEmail())
                .setStreet(customer.getStreet())
                .setCity(customer.getCity())
                .build();
            responseObserver.onNext(response);
        } else {
            responseObserver.onError(new RuntimeException("Customer not found"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getCustomerList(Empty request, StreamObserver<CustomerProto.CustomerListResponse> responseObserver) {
        // get all customers
        Collection<Customer> customers = findAllCustomers();
        // new a builder for the response
        CustomerProto.CustomerListResponse.Builder responseBuilder = CustomerProto.CustomerListResponse.newBuilder();
        // inject information into the builder
        for (Customer customer : customers) {
            CustomerProto.Customer customerResponse = CustomerProto.Customer.newBuilder()
                    .setCustomerId(customer.getId())
                    .setFirstname(customer.getFirstname())
                    .setName(customer.getName())
                    .setEmail(customer.getEmail())
                    .setStreet(customer.getStreet())
                    .setCity(customer.getCity())
                    .build();
            responseBuilder.addCustomers(customerResponse); // add each customer to the customerListResponse
        }
            CustomerProto.CustomerListResponse customerListResponse = responseBuilder.build();
            responseObserver.onNext(customerListResponse);
            System.out.println("Received customerList gRPC request with the customer information:\n" + customerListResponse.getCustomers(1));
            responseObserver.onCompleted();
    }


        // implement findItemById
        private Customer findCustomerById(long customerId){
            return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Item not found"));
        }

        // implement findAllItems
        public Collection<Customer> findAllCustomers() {
            List<Customer> customers = new ArrayList<>();
            customerRepository.findAll().forEach(customers::add);
            return customers;
        }
}