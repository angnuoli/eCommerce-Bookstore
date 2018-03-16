package com.lan.bookstore.config;

import com.lan.bookstore.service.impl.UserSecurityService;
import com.lan.bookstore.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class SecurityConfigCustomer extends WebSecurityConfigurerAdapter {
	private final UserSecurityService userSecurityService;

    @Autowired
    public SecurityConfigCustomer(UserSecurityService userSecurityService) {
        this.userSecurityService = userSecurityService;
    }

    private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
	}
	
	private static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/image/**",
			"/",
			"/newUser",
			"/forgetPassword",
			"/signUp",
			"/login",
			"/fonts/**",
			"/bookshelf",
			"/bookDetail/**",
			"/hours",
			"/faq",
			"/searchByCategory",
			"/searchBook"
	};
	
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS)
					.permitAll().anyRequest().authenticated();
		
		http
			.csrf().disable().cors().disable()
			.formLogin()
				.failureUrl("/login?error")
				.defaultSuccessUrl("/")
				.loginPage("/login")
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout").permitAll();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
}
