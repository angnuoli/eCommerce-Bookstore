package com.lan.bookstore.service;

import com.lan.bookstore.domain.User;
import com.lan.bookstore.domain.UserBilling;
import com.lan.bookstore.domain.UserPayment;
import com.lan.bookstore.domain.UserShipping;
import com.lan.bookstore.domain.security.PasswordResetToken;
import com.lan.bookstore.domain.security.UserRole;

import java.util.Set;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	void createPasswordResetTokenForUser(final User user, final String token);
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);

	void setUserDefaultPayment(Long defaultUserPaymentId, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultShipping(Long defaultUserShippingId, User user);
}
