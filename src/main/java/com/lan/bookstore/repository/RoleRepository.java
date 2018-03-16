package com.lan.bookstore.repository;

import com.lan.bookstore.domain.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByName(String name);
	
}
