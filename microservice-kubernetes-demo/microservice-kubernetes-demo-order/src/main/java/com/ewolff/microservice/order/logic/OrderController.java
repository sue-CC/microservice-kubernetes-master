package com.ewolff.microservice.order.logic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.order.clients.Customer;
import com.ewolff.microservice.order.clients.CustomerClient;
import com.ewolff.microservice.order.clients.Item;

@RestController
@RequestMapping("/orders") // Base URL for all requests in this controller
public class OrderController {

    private OrderRepository orderRepository;

    private OrderService orderService;

    private CustomerClient customerClient;
    private CatalogClient catalogClient;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderRepository orderRepository, CustomerClient customerClient,
                           CatalogClient catalogClient) {
        this.orderRepository = orderRepository;
        this.customerClient = customerClient;
        this.catalogClient = catalogClient;
        this.orderService = orderService;
    }

    @GetMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Item>> getItems() {
        return new ResponseEntity<>(catalogClient.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Customer>> getCustomers() {
        return new ResponseEntity<>(customerClient.findAll(), HttpStatus.OK);
    }

    // get order lists
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Order>> getOrderList() {
        return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
    }

    // get order by id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderById(@PathVariable("id") long id) {
        return orderRepository.findById(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // create order through orderService.order(order)
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.order(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // delete order
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteOrder(@PathVariable("id") long id) {
        if (!orderRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderRepository.deleteById(id);
        return new ResponseEntity<>("Order deleted successfully!",HttpStatus.OK);
    }
}