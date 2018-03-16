package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.*;
import com.lan.bookstore.repository.OrderRepository;
import com.lan.bookstore.service.CartItemService;
import com.lan.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
	
	private final OrderRepository orderRepository;
	
	private final CartItemService cartItemService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
    }

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
		return orderRepository.findById(id).get();
	}
	
	public Order save(Order order) {
		return orderRepository.save(order);
	}
}
