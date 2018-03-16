package com.lan.bookstore.controller.customerController;

import com.lan.bookstore.domain.*;
import com.lan.bookstore.service.*;
import com.lan.bookstore.utility.MailConstructor;
import com.lan.bookstore.utility.USConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Controller
public class CheckoutController {
	
	private static String templatePath = "customer/";
	
	private final UserService userService;

	private final CartItemService cartItemService;

	private final ShippingAddressService shippingAddressService;

	private final BillingAddressService billingAddressService;

	private final PaymentService paymentService;

	private final UserShippingService userShippingService;

	private final UserPaymentService userPaymentService;

	private final ShoppingCartService shoppingCartService;

	private final JavaMailSender mailSender;

	private final OrderService orderService;

	private final MailConstructor mailConstructor;

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

    @Autowired
    public CheckoutController(ShoppingCartService shoppingCartService, UserService userService, CartItemService cartItemService, ShippingAddressService shippingAddressService, BillingAddressService billingAddressService, PaymentService paymentService, UserShippingService userShippingService, UserPaymentService userPaymentService, JavaMailSender mailSender, OrderService orderService, MailConstructor mailConstructor) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.shippingAddressService = shippingAddressService;
        this.billingAddressService = billingAddressService;
        this.paymentService = paymentService;
        this.userShippingService = userShippingService;
        this.userPaymentService = userPaymentService;
        this.mailSender = mailSender;
        this.orderService = orderService;
        this.mailConstructor = mailConstructor;
    }

    @RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long shoppingCartId,
                           @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
                           Principal principal) {
		User user = userService.findByUsername(principal.getName());

		if (!shoppingCartId.equals(user.getShoppingCart().getId())) {
			return "badRequestPage";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyShoppingCart", true);
			return "forward:/shoppingCart/cart";
		}

		for (CartItem cartItem : cartItemList) {
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		for (UserShipping userShipping : userShippingList) {
			if (userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		for (UserPayment userPayment : userPaymentList) {
			if (userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}

		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());

		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);

		model.addAttribute("classActiveShipping", true);

		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return templatePath + "checkout";

	}

	@RequestMapping("/orderHistory")
	public String orderHistory(Model model) {
		return templatePath + "/myPorfile";
	}

	@RequestMapping("/submitOrder")
	public String submitOrder(Model model) {
		return "redirect:/order-history";
	}

	@RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
                               @ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
                               @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
                               @ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);

		if (cartItemList.size() == 0) {
			model.addAttribute("emptyShoppingCart", true);
			return "forward:/shoppingCart/cart";
		}

		model.addAttribute("cartItemList", cartItemList);

		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty()
				|| shippingAddress.getShippingAddressCountry().isEmpty() || payment.getCvc() == 0
				|| billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressCountry().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()) {
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		}

		User user = userService.findByUsername(principal.getName());

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod,
				user);

		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

		shoppingCartService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;

		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}

		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		order.setOrderStatus("placed");
		orderService.save(order);
		model.addAttribute("order", order);

		return templatePath + "orderSubmittedPage";
	}

	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal,
                                     Model model) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);

		if (!userShipping.getUser().getId().equals(user.getId())) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			model.addAttribute("classActiveShipping", true);

			return templatePath + "checkout";
		}
	}

	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal,
                                   Model model) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		if (!userPayment.getUser().getId().equals(user.getId())) {
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);

			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

			billingAddressService.setByUserBilling(userBilling, billingAddress);

			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());

			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);

			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();

			model.addAttribute("emptyPaymentList", false);

			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("classActivePayment", true);

			return templatePath + "checkout";
		}
	}
}
