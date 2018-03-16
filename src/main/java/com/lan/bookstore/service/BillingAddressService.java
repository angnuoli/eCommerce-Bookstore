package com.lan.bookstore.service;

import com.lan.bookstore.domain.BillingAddress;
import com.lan.bookstore.domain.UserBilling;

public interface BillingAddressService {

	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
