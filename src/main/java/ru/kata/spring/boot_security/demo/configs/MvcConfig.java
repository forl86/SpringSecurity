package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/user").setViewName("userPage");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/admin").setViewName("adminPage");
        registry.addViewController("/add").setViewName("editPage");
        registry.addViewController("/login").setViewName("loginPage");
    }
}
