package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.CartItem;
import com.lan.bookstore.domain.ShoppingCart;
import com.lan.bookstore.repository.ShoppingCartRepository;
import com.lan.bookstore.service.CartItemService;
import com.lan.bookstore.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	private final CartItemService cartItemService;

	private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartServiceImpl(CartItemService cartItemService, ShoppingCartRepository shoppingCartRepository) {
        this.cartItemService = cartItemService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		BigDecimal cartTotal = new BigDecimal(0);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() > 0) {
				cartItemService.updateCartItem(cartItem);
				cartTotal = cartTotal.add(cartItem.getSubTotal());
			}
		}
		
		shoppingCart.setGrandTotal(cartTotal);
		
		shoppingCartRepository.save(shoppingCart);
		
		return shoppingCart;
	}
	
	public void clearShoppingCart(ShoppingCart shoppingCart) {
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);
		}
		
		shoppingCart.setGrandTotal(new BigDecimal(0));
		shoppingCartRepository.save(shoppingCart);
	}
}
