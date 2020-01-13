package com.example.demo;

import com.example.demo.properties.JwtAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
public class HelloContoller {

    @Autowired
    JwtAuthProperties jwtAuthProperties;

    @PostConstruct
    public void init(){

        log.debug(jwtAuthProperties.getSecretKey());
        log.debug(jwtAuthProperties.getExpireLength() + "");
    }

    @GetMapping("/api/hello")
    public String hello(){
        return "Hello world.";
    }

    @GetMapping("/api/test")
    public String test(){
        return "test!";
    }

}
