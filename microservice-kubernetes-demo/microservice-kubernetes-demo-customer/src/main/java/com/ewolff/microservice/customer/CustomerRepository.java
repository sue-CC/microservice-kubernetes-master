package com.ewolff.microservice.customer;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends
		PagingAndSortingRepository<Customer, Long> {

//	Customer findByName(@Param("name") String name);

}
