package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.UserPayment;
import com.lan.bookstore.repository.UserPaymentRepository;
import com.lan.bookstore.service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {
	
	private final UserPaymentRepository userPaymentRepository;

    @Autowired
    public UserPaymentServiceImpl(UserPaymentRepository userPaymentRepository) {
        this.userPaymentRepository = userPaymentRepository;
    }

    public UserPayment findById(Long id) {
		return userPaymentRepository.findById(id).get();
	}
	
	public void removeById(Long id) {
		userPaymentRepository.deleteById(id);
	}
}

