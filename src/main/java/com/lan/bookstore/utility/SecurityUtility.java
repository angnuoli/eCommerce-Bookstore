package com.lan.bookstore.utility;

import com.lan.bookstore.config.DataLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class SecurityUtility {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/config/application-context.xml");
	
	private static final byte[] SALT = context.getBean("dataLoader", DataLoader.class).getSalt(); // Salt should be protected carefully
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT));
	}
	
	@Bean
	public static String randomPassword() {
		String SALTCHARS = "ABCEFGHIJKLMNOPQRSTUVWXYZ1234567890abcefghijklmnopqrstuvwxyz";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		
		String saltStr = salt.toString();
		return saltStr;
	}
}
