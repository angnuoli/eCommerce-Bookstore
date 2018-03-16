package com.lan.bookstore;

import com.lan.bookstore.domain.User;
import com.lan.bookstore.domain.security.Role;
import com.lan.bookstore.domain.security.UserRole;
import com.lan.bookstore.service.UserService;
import com.lan.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ECommerceBookstoreApplication {

    @Autowired
    public ECommerceBookstoreApplication(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
    }

    public static void main(String[] args) {
		SpringApplication.run(ECommerceBookstoreApplication.class, args);
	}

    protected final UserService userService;

    private final Environment env;

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
