package com.lan.bookstore.service;

import com.lan.bookstore.domain.ShippingAddress;
import com.lan.bookstore.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
