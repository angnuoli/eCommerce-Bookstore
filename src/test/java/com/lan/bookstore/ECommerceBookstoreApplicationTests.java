package com.lan.bookstore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/application-context.xml"})
@SpringBootTest
public class ECommerceBookstoreApplicationTests {

	@Test
	public void contextLoads() {
	}

}
