package com.lan.bookstore.controller.customerController;

import com.lan.bookstore.domain.Book;
import com.lan.bookstore.domain.CartItem;
import com.lan.bookstore.domain.ShoppingCart;
import com.lan.bookstore.domain.User;
import com.lan.bookstore.service.BookService;
import com.lan.bookstore.service.CartItemService;
import com.lan.bookstore.service.ShoppingCartService;
import com.lan.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	private static final String templatePath = "customer/";

	private final UserService userService;
	
	private final CartItemService cartItemService;
	
	private final ShoppingCartService shoppingCartService;
	
	private final BookService bookService;

    @Autowired
    public ShoppingCartController(UserService userService, CartItemService cartItemService, ShoppingCartService shoppingCartService, BookService bookService) {
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
    }

    @RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);

		return templatePath + "shoppingCart";
	}
	
	@RequestMapping("/addItem")
	public String addItem(
            @ModelAttribute("book") Book book,
            @ModelAttribute("qty") String qty,
            Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		book = bookService.findOne(book.getId());
		
		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id="+book.getId();
		}
		
		cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);
		
		return "forward:/bookDetail?id="+book.getId();
	}
	
	@RequestMapping("/updateCartItem")
	public String updateCartItem(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeCartItem")
	public String removeCartItem(@RequestParam("id") Long id){
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}
