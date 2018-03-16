package com.lan.bookstore.service;

import com.lan.bookstore.domain.*;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress, Payment payment, String shippingMethod, User user);

	Order findOne(Long id);
	
	Order save(Order order);
}
