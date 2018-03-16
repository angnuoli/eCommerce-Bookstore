package com.lan.bookstore.controller.customerController;

import com.lan.bookstore.domain.Book;
import com.lan.bookstore.domain.User;
import com.lan.bookstore.service.BookService;
import com.lan.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {
	
	private static String templatePath = "customer/";
	
	private final UserService userService;
	
	private final BookService bookService;

    @Autowired
    public SearchController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @RequestMapping("/searchByCategory")
	public String searchByCategory(
            @RequestParam("category") String category,
            Model model, Principal principal
			) {
		if (principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		String classActiveCategory = "active" + category;
		classActiveCategory = classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory = classActiveCategory.replaceAll("&", "");
		model.addAttribute(classActiveCategory, true);
		
		List<Book> bookList = bookService.findByCategory(category);
		
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return templatePath + "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return templatePath + "bookshelf";
	}
	
	@RequestMapping("searchBook")
	public String searchBook(
            @ModelAttribute("keyword") String keyword,
            Model model, Principal principal
			) {
		if (principal!=null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
		
		if (bookList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return templatePath + "bookshelf";
		}
		
		model.addAttribute("bookList", bookList);
		
		return templatePath + "bookshelf";
	}
}
