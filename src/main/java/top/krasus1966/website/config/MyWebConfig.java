package top.krasus1966.website.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Krasus1966
 * @date 2019/11/12 15:22
 **/
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/", "classpath:/static/");
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/user/**")
                .addPathPatterns("/admin/**")
                .addPathPatterns("/comments")
                .addPathPatterns("/user")
                .addPathPatterns("/user/blogs/input")
                .addPathPatterns("/user/blogs/*/input")
                .addPathPatterns("/blog/archives")
                .excludePathPatterns("/comments/*")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/toLogin")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/toRegister")
                .excludePathPatterns("/user/*/profile")
                .excludePathPatterns("/user/*/profile/*")
        ;
    }
}
