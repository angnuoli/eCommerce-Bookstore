package com.lan.bookstore.controller.customerController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(locations = {
        "classpath*:/config/application-context.xml",
})
public class HomeControllerTest {
    private MockMvc mvc;


//    @Autowired
//    HomeController homeController;

    @Mock
    private HomeController homeController;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB_INF/views/");
        viewResolver.setSuffix(".jsp");

        mvc = MockMvcBuilders.standaloneSetup(homeController).setViewResolvers(viewResolver).build();
    }

    @Test
    public void faqTest() throws Exception {
        mvc.perform(post("/faq").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("faq"));
    }
}
