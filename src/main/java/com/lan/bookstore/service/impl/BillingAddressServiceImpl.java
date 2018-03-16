package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.BillingAddress;
import com.lan.bookstore.domain.UserBilling;
import com.lan.bookstore.service.BillingAddressService;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {

	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
		
		return billingAddress;
	}
}
