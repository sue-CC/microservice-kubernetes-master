package com.ewolff.microservice.customer.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ewolff.microservice.customer.Customer;
import com.ewolff.microservice.customer.CustomerRepository;

@RestController
@RequestMapping("/customer") // Base URL for all requests in this controller
public class CustomerController {

	private CustomerRepository customerRepository;

	@Autowired
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	// get customer by id
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
		return customerRepository.findById(id)
				.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// get customer list
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Customer>> getCustomerList() {
		return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
	}

	// create customer
	@PostMapping(value = "/form", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer, HttpServletRequest httpRequest) {
		Customer savedCustomer = customerRepository.save(customer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
	}

	// update customer
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> putCustomer(@PathVariable("id") long id, @RequestBody Customer customer, HttpServletRequest httpRequest) {
		if (!customerRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		customer.setId(id);
		Customer updatedCustomer = customerRepository.save(customer);
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}

	// delete customer
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		if (!customerRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		customerRepository.deleteById(id);
		return new ResponseEntity<>("Customer  deleted successfully!", HttpStatus.OK);
	}
}