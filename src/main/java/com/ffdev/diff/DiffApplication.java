package com.ffdev.diff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DiffApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiffApplication.class, args);
    }

}
