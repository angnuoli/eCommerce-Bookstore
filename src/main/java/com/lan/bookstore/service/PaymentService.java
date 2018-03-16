package com.lan.bookstore.service;

import com.lan.bookstore.domain.Payment;
import com.lan.bookstore.domain.UserPayment;

public interface PaymentService {
    Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
