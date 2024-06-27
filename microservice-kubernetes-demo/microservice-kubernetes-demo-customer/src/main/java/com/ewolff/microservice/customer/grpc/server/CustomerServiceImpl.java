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
        logger.info("Received getCustomer gRPC request for customerId: {}", request.getCustomerId());
        long customerId = request.getCustomerId();
        try {
            Customer customer = findCustomerById(customerId);
            CustomerProto.CustomerResponse response = CustomerProto.CustomerResponse.newBuilder()
                    .setCustomerId(customer.getId())
                    .setName(customer.getName())
                    .setFirstname(customer.getFirstname())
                    .setEmail(customer.getEmail())
                    .setStreet(customer.getStreet())
                    .setCity(customer.getCity())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Customer not found for customerId: {}", customerId, e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getCustomerList(Empty request, StreamObserver<CustomerProto.CustomerListResponse> responseObserver) {
        logger.info("Received getCustomerList gRPC request");
        Collection<Customer> customers = findAllCustomers();
        CustomerProto.CustomerListResponse.Builder responseBuilder = CustomerProto.CustomerListResponse.newBuilder();
        for (Customer customer : customers) {
            CustomerProto.Customer customerResponse = CustomerProto.Customer.newBuilder()
                    .setCustomerId(customer.getId())
                    .setFirstname(customer.getFirstname())
                    .setName(customer.getName())
                    .setEmail(customer.getEmail())
                    .setStreet(customer.getStreet())
                    .setCity(customer.getCity())
                    .build();
            responseBuilder.addCustomers(customerResponse);
        }
        CustomerProto.CustomerListResponse customerListResponse = responseBuilder.build();
        responseObserver.onNext(customerListResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void createCustomer(CustomerProto.CreateCustomerRequest request, StreamObserver<CustomerProto.CustomerResponse> responseObserver) {
        logger.info("Received createCustomer gRPC request");
        Customer customer = new Customer();
        customer.setFirstname(request.getFirstname());
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setStreet(request.getStreet());
        customer.setCity(request.getCity());

        customer = customerRepository.save(customer);
        CustomerProto.CustomerResponse response = CustomerProto.CustomerResponse.newBuilder()
                .setCustomerId(customer.getId())
                .setFirstname(customer.getFirstname())
                .setName(customer.getName())
                .setEmail(customer.getEmail())
                .setStreet(customer.getStreet())
                .setCity(customer.getCity())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCustomer(CustomerProto.UpdateCustomerRequest request, StreamObserver<CustomerProto.CustomerResponse> responseObserver) {
        logger.info("Received updateCustomer gRPC request for customerId: {}", request.getCustomerId());
        try {
            Customer customer = findCustomerById(request.getCustomerId());
            customer.setFirstname(request.getFirstname());
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setStreet(request.getStreet());
            customer.setCity(request.getCity());

            customer = customerRepository.save(customer);
            CustomerProto.CustomerResponse response = CustomerProto.CustomerResponse.newBuilder()
                    .setCustomerId(customer.getId())
                    .setFirstname(customer.getFirstname())
                    .setName(customer.getName())
                    .setEmail(customer.getEmail())
                    .setStreet(customer.getStreet())
                    .setCity(customer.getCity())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Customer not found for customerId: {}", request.getCustomerId(), e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteCustomer(CustomerProto.CustomerRequest request, StreamObserver<CustomerProto.SuccessResponse> responseObserver) {
        logger.info("Received deleteCustomer gRPC request for customerId: {}", request.getCustomerId());
        long customerId = request.getCustomerId();
        try {
            Customer customer = findCustomerById(customerId);
            customerRepository.delete(customer);
            CustomerProto.SuccessResponse response = CustomerProto.SuccessResponse.newBuilder()
                    .setSuccessMessage("Customer successfully deleted!")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            logger.error("Customer not found for customerId: {}", customerId, e);
            responseObserver.onError(e);
        }
    }

    private Customer findCustomerById(long customerId){
        return customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Collection<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }
}
