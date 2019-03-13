package com.raoke007.springinaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringinactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringinactionApplication.class, args);
    }

}
