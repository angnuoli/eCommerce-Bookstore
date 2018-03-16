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
@Order(1)
public class SecurityConfigAdmin extends WebSecurityConfigurerAdapter {
	private final UserSecurityService userSecurityService;

    @Autowired
    public SecurityConfigAdmin(UserSecurityService userSecurityService) {
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
			"/searchBook",
			"/adminportal/login"
	};
	
	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers(PUBLIC_MATCHERS)
					.permitAll().anyRequest().authenticated();
		
		http
			.antMatcher("/adminportal/**")
			.authorizeRequests()
			.anyRequest()
			.hasAuthority("ROLE_ADMIN")
			.and()
			.formLogin()
				.failureUrl("/adminportal/login?error")
				.loginPage("/adminportal/login")
				.defaultSuccessUrl("/adminportal/")
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout").permitAll()
				.and()
			.csrf().disable().cors().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
}
