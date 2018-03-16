package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.*;
import com.lan.bookstore.repository.BookToCartItemRepository;
import com.lan.bookstore.repository.CartItemRepository;
import com.lan.bookstore.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

	private final CartItemRepository cartItemRepository;

	private final BookToCartItemRepository bookToCartItemRepository;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, BookToCartItemRepository bookToCartItemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookToCartItemRepository = bookToCartItemRepository;
    }

    public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		return (List<CartItem>) cartItemRepository.findByShoppingCart(shoppingCart);
	}

	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getBook().getOurPrice())
				.multiply(new BigDecimal(cartItem.getQty()));

		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubTotal(bigDecimal);

		cartItemRepository.save(cartItem);

		return cartItem;
	}

	public CartItem addBookToCartItem(Book book, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());

		for (CartItem cartItem : cartItemList) {
			if (book.getId().equals(cartItem.getBook().getId())) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}

		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);

		cartItem.setQty(qty);
		cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItemRepository.save(cartItem);

		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);

		return cartItem;
	}

	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).get();
	}
	
	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}

	public void removeCartItem(CartItem cartItem) {
		bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
}
