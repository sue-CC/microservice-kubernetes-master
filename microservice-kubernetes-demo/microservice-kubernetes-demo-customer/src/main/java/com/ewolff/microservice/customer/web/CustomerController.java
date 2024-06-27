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

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
		return customerRepository.findById(id)
				.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Customer>> getCustomerList() {
		return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/form", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getAddForm() {
		return new ResponseEntity<>(new Customer(), HttpStatus.OK);
	}

	@PostMapping(value = "/form", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> postCustomer(@RequestBody Customer customer, HttpServletRequest httpRequest) {
		Customer savedCustomer = customerRepository.save(customer);
		return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> putCustomer(@PathVariable("id") long id, @RequestBody Customer customer, HttpServletRequest httpRequest) {
		if (!customerRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		customer.setId(id);
		Customer updatedCustomer = customerRepository.save(customer);
		return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		if (!customerRepository.existsById(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		customerRepository.deleteById(id);
		return new ResponseEntity<>("Customer  deleted successfully!", HttpStatus.OK);
	}

}


//package com.ewolff.microservice.customer.web;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.ewolff.microservice.customer.Customer;
//import com.ewolff.microservice.customer.CustomerRepository;
//
//@Controller
//public class CustomerController {
//
//	private CustomerRepository customerRepository;
//
//	@Autowired
//	public CustomerController(CustomerRepository customerRepository) {
//		this.customerRepository = customerRepository;
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView customer(@PathVariable("id") long id) {
//		return new ModelAndView("customer", "customer",
//				customerRepository.findById(id).get());
//	}
//
//	@RequestMapping("/list.html")
//	public ModelAndView customerList() {
//		return new ModelAndView("customerlist", "customers",
//				customerRepository.findAll());
//	}
//
//	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
//	public ModelAndView add() {
//		return new ModelAndView("customer", "customer", new Customer());
//	}
//
//	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
//	public ModelAndView post(Customer customer, HttpServletRequest httpRequest) {
//		customer = customerRepository.save(customer);
//		return new ModelAndView("success");
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
//	public ModelAndView put(@PathVariable("id") long id, Customer customer,
//			HttpServletRequest httpRequest) {
//		customer.setId(id);
//		customerRepository.save(customer);
//		return new ModelAndView("success");
//	}
//
//	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
//	public ModelAndView delete(@PathVariable("id") long id) {
//		customerRepository.deleteById(id);
//		return new ModelAndView("success");
//	}
//
//}
