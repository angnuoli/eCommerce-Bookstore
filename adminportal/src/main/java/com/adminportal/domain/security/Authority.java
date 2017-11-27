package com.adminportal.domain.security;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8027895136212048416L;
	private final String authority;
	
	public Authority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}
}