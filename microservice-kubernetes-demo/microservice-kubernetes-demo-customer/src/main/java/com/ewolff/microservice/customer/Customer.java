package com.ewolff.microservice.customer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Customer {

	@Id
	@GeneratedValue
	private Long id;

	@Lob
	@Size(max = 150000)
	@Column(nullable = false, length = 150000)
	private String name;

	@Lob
	@Size(max = 150000)
	@Column(nullable = false, length = 150000)
	private String firstname;

	@Lob
	@Size(max = 150000)
	@Column(nullable = false, length = 150000)
	@Email
	private String email;

	@Lob
	@Size(max = 150000)
	@Column(nullable = false, length = 150000)
	private String street;

	@Lob
	@Size(max = 150000)
	@Column(nullable = false, length = 150000)
	private String city;

	public Customer() {
		super();
	}

	public Customer(String firstname, String name, String email, String street,
			String city) {
		super();
		this.name = name;
		this.firstname = firstname;
		this.email = email;
		this.street = street;
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);

	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

}
