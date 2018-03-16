package com.lan.bookstore.repository;

import com.lan.bookstore.domain.CartItem;
import com.lan.bookstore.domain.Order;
import com.lan.bookstore.domain.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	List<CartItem> findByOrder(Order order);
}
