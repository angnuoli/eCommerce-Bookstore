package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.UserShipping;
import com.lan.bookstore.repository.UserShippingRepository;
import com.lan.bookstore.service.UserShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShippingServiceImpl implements UserShippingService {
	
	private final UserShippingRepository userShippingRepository;

    @Autowired
    public UserShippingServiceImpl(UserShippingRepository userShippingRepository) {
        this.userShippingRepository = userShippingRepository;
    }

    public UserShipping findById(Long id) {
		return userShippingRepository.findById(id).get();
	}
	
	public void removeById(Long id) {
		userShippingRepository.deleteById(id);
	}
}
