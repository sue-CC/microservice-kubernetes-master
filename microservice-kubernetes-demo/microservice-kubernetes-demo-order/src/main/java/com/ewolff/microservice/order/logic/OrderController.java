package com.ewolff.microservice.order.logic;

import java.util.Collection;
import java.util.Optional;

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
        return new ResponseEntity<>("Order is deleted successfully!",HttpStatus.OK);
    }
}



//package com.ewolff.microservice.order.logic;
//
//import java.util.Collection;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.ewolff.microservice.order.clients.CatalogClient;
//import com.ewolff.microservice.order.clients.Customer;
//import com.ewolff.microservice.order.clients.CustomerClient;
//import com.ewolff.microservice.order.clients.Item;
//
//@Controller
////@RequestMapping("/order")
//class OrderController {
//
//	private OrderRepository orderRepository;
//
//	private OrderService orderService;
//
//	private CustomerClient customerClient;
//	private CatalogClient catalogClient;
//
//	@Autowired
//	private OrderController(OrderService orderService,
//							OrderRepository orderRepository, CustomerClient customerClient,
//							CatalogClient catalogClient) {
//		super();
//		this.orderRepository = orderRepository;
//		this.customerClient = customerClient;
//		this.catalogClient = catalogClient;
//		this.orderService = orderService;
//	}
//
//	@ModelAttribute("items")
//	public Collection<Item> items() {
//		return catalogClient.findAll();
//	}
//
//	@ModelAttribute("customers")
//	public Collection<Customer> customers() {
//		return customerClient.findAll();
//	}
//
//	@RequestMapping("/")
//	public ModelAndView orderList() {
//		return new ModelAndView("orderlist", "orders",
//				orderRepository.findAll());
//	}
//
//	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
//	public ModelAndView form() {
//		return new ModelAndView("orderForm", "order", new Order());
//	}
//
//	@RequestMapping(value = "/line", method = RequestMethod.POST)
//	public ModelAndView addLine(Order order) {
//		order.addLine(0, catalogClient.findAll().iterator().next().getItemId());
//		return new ModelAndView("orderForm", "order", order);
//	}
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public ModelAndView get(@PathVariable("id") long id) {
//		return new ModelAndView("order", "order", orderRepository.findById(id).get());
//	}
//
//	@RequestMapping(value = "/", method = RequestMethod.POST)
//	public ModelAndView post(Order order) {
//		order = orderService.order(order);
//		return new ModelAndView("success");
//	}
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public ModelAndView post(@PathVariable("id") long id) {
//		orderRepository.deleteById(id);
//
//		return new ModelAndView("success");
//	}
//
//}