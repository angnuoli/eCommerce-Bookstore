package com.lan.bookstore.service;

import com.lan.bookstore.domain.UserPayment;

public interface UserPaymentService {
	UserPayment findById(Long id);
	
	void removeById(Long id);
}
