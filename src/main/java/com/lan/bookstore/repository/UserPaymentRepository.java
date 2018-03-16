package com.lan.bookstore.repository;

import com.lan.bookstore.domain.UserPayment;
import org.springframework.data.repository.CrudRepository;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long> {

}
