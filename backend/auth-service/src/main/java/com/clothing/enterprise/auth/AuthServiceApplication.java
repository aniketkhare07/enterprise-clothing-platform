package com.clothing.enterprise.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// SCAN BOTH PACKAGES
@ComponentScan(basePackages = {"com.clothing.enterprise.auth", "com.clothing.enterprise.common"})
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}