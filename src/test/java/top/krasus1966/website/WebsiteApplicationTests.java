package top.krasus1966.website;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.krasus1966.website.service.BlogService;

@SpringBootTest
class WebsiteApplicationTests {

    @Autowired
    private BlogService blogService;
    @Test
    void contextLoads() {
        System.out.println(blogService.getBlog(1L));
    }

}
