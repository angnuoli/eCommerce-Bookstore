package com.bookstore.controller.adminController;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminportal")
public class AdminHomeController {
	
	private String templatePath = "admin/";
	
	@RequestMapping("/")
	public String index() {
		return "redirect:home";
	}
	
	@RequestMapping("/home")
	public String home(Principal principal) {
		
		return templatePath + "home";
	}
	
	@RequestMapping("/login") 
	public String login() {
		return templatePath + "login";
	}
}

