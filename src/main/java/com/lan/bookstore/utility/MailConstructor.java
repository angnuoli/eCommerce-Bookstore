package com.lan.bookstore.utility;

import com.lan.bookstore.domain.Order;
import com.lan.bookstore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Component
public class MailConstructor {
	
	private final Environment env;

    protected final TemplateEngine templateEngine;

    @Autowired
    public MailConstructor(Environment env, TemplateEngine templateEngine) {
        this.env = env;
        this.templateEngine = templateEngine;
    }

    public SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user, String password
			) {
		String url = contextPath + "/newUser?token="+token;
		String message = "Please click on this link to verify your email and edit your personal information. \n\n"+ url + "\n\nYour password is: \n\n"+ password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Li's Bookstore - New User");
		email.setText(message);
		email.setFrom(env.getProperty("support.email"));
		
		return email;
	}
	
	public MimeMessagePreparator constructOrderConfirmationEmail(
			User user, 
			Order order,
			Locale locale
			) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("customer/orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
            email.setTo(user.getEmail());
            email.setSubject("Order Confirmaiton - "+order.getId());
            email.setText(text, true);
            email.setFrom(env.getProperty("support.email"));
        };
		
		return messagePreparator;
	}
}
