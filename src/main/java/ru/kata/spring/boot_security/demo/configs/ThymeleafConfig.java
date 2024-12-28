package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

public class ThymeleafConfig {
    @Bean
    public Java8TimeDialect springSecurityDialect() {
        return new Java8TimeDialect();
    }
}
