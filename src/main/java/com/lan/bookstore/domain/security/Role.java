package com.lan.bookstore.domain.security;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
	
	@Id
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<UserRole> userRoles = new HashSet<>();

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	
}
