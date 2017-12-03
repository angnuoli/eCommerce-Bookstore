package com.bookstore.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.BillingAddress;
import com.bookstore.domain.Book;
import com.bookstore.domain.CartItem;
import com.bookstore.domain.Order;
import com.bookstore.domain.Payment;
import com.bookstore.domain.ShippingAddress;
import com.bookstore.domain.ShoppingCart;
import com.bookstore.domain.User;
import com.bookstore.repository.OrderRepository;
import com.bookstore.service.CartItemService;
import com.bookstore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart, 
			ShippingAddress shippingAddress, 
			BillingAddress billingAddress, 
			Payment payment, 
			String shippingMethod, 
			User user) {
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod(shippingMethod);
		order.setPayment(payment);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
				
		for(CartItem cartItem : cartItemList) {
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		order.setOrderStatus("created");
		
		return order;
	}
	
	public Order findOne(Long id) {
		return orderRepository.findOne(id);
	}
	
	public Order save(Order order) {
		return orderRepository.save(order);
	}
}
