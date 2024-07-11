package com.ewolff.microservice.customer.grpc.client;

import com.ewolff.microservice.customer.Customer;
import com.ewolff.microservice.customer.CustomerRepository;
import com.ewolff.microservice.customer.grpc.CustomerProto;
import com.ewolff.microservice.customer.grpc.CustomerServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class customerClientImpl implements customerClient {
    private final CustomerRepository customerRepository;
    private static final Logger logger = Logger.getLogger(customerClientImpl.class.getName());
    private final CustomerServiceGrpc.CustomerServiceBlockingStub customerService;
    private final ManagedChannel channel;

    @Autowired// construct client for accessing catalog server using the channel
    public customerClientImpl(@Value("${customer.service.host:localhost}") String host,
                             @Value("${customer.service.port:9090}") int port, CustomerRepository customerRepository) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext().build();
        this.customerService = CustomerServiceGrpc.newBlockingStub(channel);
        this.customerRepository = customerRepository;
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }

    @Override
    public Customer getCustomer(Long id) {
        CustomerProto.CustomerRequest request = CustomerProto.CustomerRequest.newBuilder().setCustomerId(id).build();
        if (!customerRepository.existsById(id)) {
            logger.warning("Customer with id " + id + " does not exist");
            return customerRepository.findById(id).orElse(null);
        }
        else {
            CustomerProto.CustomerResponse response = customerService.getCustomer(request);
            return new Customer(response.getName(), response.getFirstname(), response.getEmail(), response.getStreet(), response.getCity());
        }
    }

    @Override
    public Collection<Customer> getCustomers() {
        Empty request = Empty.newBuilder().build();
        CustomerProto.CustomerListResponse response = customerService.getCustomerList(request);
        return response.getCustomersList().stream()
                .map(customer -> new Customer(customer.getFirstname(), customer.getName(), customer.getEmail(), customer.getStreet(), customer.getCity()))
                .collect(Collectors.toList());
    }


    @Override
    public Customer createCustomer(Customer customer) {
        CustomerProto.CreateCustomerRequest request = CustomerProto.CreateCustomerRequest.newBuilder()
                .setFirstname(customer.getFirstname())
                .setName(customer.getName())
                .setEmail(customer.getEmail())
                .setStreet(customer.getStreet())
                .setCity(customer.getCity())
                .build();
        CustomerProto.CustomerResponse response = customerService.createCustomer(request);
        return new Customer(response.getFirstname(), response.getName(), response.getEmail(), response.getStreet(), response.getCity());
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        CustomerProto.UpdateCustomerRequest request = CustomerProto.UpdateCustomerRequest.newBuilder()
                .setCustomerId(customer.getId())
                .setFirstname(customer.getFirstname())
                .setName(customer.getName())
                .setEmail(customer.getEmail())
                .setStreet(customer.getStreet())
                .setCity(customer.getCity())
                .build();
        CustomerProto.CustomerResponse response = customerService.updateCustomer(request);
        return new Customer(response.getFirstname(), response.getName(), response.getEmail(), response.getStreet(), response.getCity());
    }

    @Override
    public String deleteCustomer(Long id) {
        CustomerProto.CustomerRequest request = CustomerProto.CustomerRequest.newBuilder().setCustomerId(id).build();
        CustomerProto.SuccessResponse response = customerService.deleteCustomer(request);
        return response.getSuccessMessage();
    }
}
