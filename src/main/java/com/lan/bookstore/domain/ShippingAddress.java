package com.lan.bookstore.domain;

import javax.persistence.*;

@Entity
public class ShippingAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	private String shippingAddressName;
	private String shippingAddressStreet1;
	private String shippingAddressStreet2;
	private String shippingAddressCity;	
	private String shippingAddressState;	
	private String shippingAddressCountry;	
	private String shippingAddressZipcode;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToOne
	private Order order;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getShippingAddressName() {
		return shippingAddressName;
	}

	public void setShippingAddressName(String shippingAddressName) {
		this.shippingAddressName = shippingAddressName;
	}

	public String getShippingAddressStreet1() {
		return shippingAddressStreet1;
	}

	public void setShippingAddressStreet1(String shippingAddressStreet1) {
		this.shippingAddressStreet1 = shippingAddressStreet1;
	}

	public String getShippingAddressStreet2() {
		return shippingAddressStreet2;
	}

	public void setShippingAddressStreet2(String shippingAddressStreet2) {
		this.shippingAddressStreet2 = shippingAddressStreet2;
	}

	public String getShippingAddressCity() {
		return shippingAddressCity;
	}

	public void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}

	public String getShippingAddressState() {
		return shippingAddressState;
	}

	public void setShippingAddressState(String shippingAddressState) {
		this.shippingAddressState = shippingAddressState;
	}

	public String getShippingAddressCountry() {
		return shippingAddressCountry;
	}

	public void setShippingAddressCountry(String shippingAddressCountry) {
		this.shippingAddressCountry = shippingAddressCountry;
	}

	public String getShippingAddressZipcode() {
		return shippingAddressZipcode;
	}

	public void setShippingAddressZipcode(String shippingAddressZipcode) {
		this.shippingAddressZipcode = shippingAddressZipcode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
	
}
