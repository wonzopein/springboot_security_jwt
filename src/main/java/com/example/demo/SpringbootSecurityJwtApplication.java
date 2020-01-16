package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@SpringBootApplication
public class SpringbootSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityJwtApplication.class, args);
    }

}
