package com.lan.bookstore.repository;

import com.lan.bookstore.domain.BookToCartItem;
import com.lan.bookstore.domain.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);
}
