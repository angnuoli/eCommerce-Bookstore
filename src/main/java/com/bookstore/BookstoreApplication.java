package com.bookstore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.bookstore.domain.User;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import com.bookstore.utility.SecurityUtility;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		createUser();
		createAdmin();
	}
	
	private void createUser() throws Exception {
		User user1 = new User();
		user1.setFirstname("John");
		user1.setLastname("Ge");
		user1.setUsername(env.getProperty("user.username"));
		user1.setPassword(SecurityUtility.passwordEncoder().encode(env.getProperty("user.password")));
		user1.setEmail(env.getProperty("user.email"));
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setId((long) 1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}
	
	private void createAdmin() throws Exception {
		User user1 = new User();
		user1.setUsername(env.getProperty("admin.username"));
		user1.setPassword(SecurityUtility.passwordEncoder().encode(env.getProperty("admin.password")));
		user1.setEmail(env.getProperty("admin.email"));
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setId((long)0);
		role1.setName("ROLE_ADMIN");
		userRoles.add(new UserRole(user1, role1));

		userService.createUser(user1, userRoles);
	}
}
