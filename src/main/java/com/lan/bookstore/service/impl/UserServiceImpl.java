package com.lan.bookstore.service.impl;

import com.lan.bookstore.domain.*;
import com.lan.bookstore.domain.security.PasswordResetToken;
import com.lan.bookstore.domain.security.UserRole;
import com.lan.bookstore.repository.*;
import com.lan.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	
	private final UserPaymentRepository userPaymentRepository;
	
	private final UserShippingRepository userShippingRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordResetTokenRepository passwordResetTokenRepository, UserPaymentRepository userPaymentRepository, UserShippingRepository userShippingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userPaymentRepository = userPaymentRepository;
        this.userShippingRepository = userShippingRepository;
    }

    @Override
	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	@Override
	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}
	
	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if (localUser != null) {
			LOG.info("User already exists. Nothing will be done.", user.getUsername());
		} else {
			for (UserRole ur : userRoles) {
				if (roleRepository.findById(ur.getRole().getId()).isPresent()) {
					roleRepository.save(ur.getRole());
				}
			}
			
			user.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			
			user.setUserShippingList(new ArrayList<>());
			user.setUserPaymentList(new ArrayList<>());
			
			localUser = userRepository.save(user);
		}
		
		return localUser;
	} 
	
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}
	
	@Override
	public void setUserDefaultPayment(Long defaultUserPaymentId, User user) {
		Iterable<UserPayment> userPaymentList = userPaymentRepository.findAll();
		
		for (UserPayment userPayment: userPaymentList) {
			if (userPayment.getId().equals(defaultUserPaymentId)) {
				userPayment.setDefaultPayment(true);
			} else {
				userPayment.setDefaultPayment(false);
			}
			userPaymentRepository.save(userPayment);
		}
	}
	
	@Override 
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		user.getUserShippingList().add(userShipping);
		save(user);
	}
	
	@Override
	public void setUserDefaultShipping(Long defaultUserShippingId, User user) {
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
		
		for (UserShipping userShipping: userShippingList) {
			if (userShipping.getId().equals(defaultUserShippingId)) {
				userShipping.setUserShippingDefault(true);
			} else {
				userShipping.setUserShippingDefault(false);
			}
			userShippingRepository.save(userShipping);
		}
	}
}
