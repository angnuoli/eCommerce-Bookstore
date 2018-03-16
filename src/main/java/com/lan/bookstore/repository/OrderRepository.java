package com.lan.bookstore.repository;

import com.lan.bookstore.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
