package com.lan.bookstore.service;

import com.lan.bookstore.domain.UserShipping;

public interface UserShippingService {
	UserShipping findById(Long id);
	
	void removeById(Long id);
}
