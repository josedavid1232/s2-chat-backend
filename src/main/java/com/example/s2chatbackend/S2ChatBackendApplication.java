package com.example.s2chatbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class S2ChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(S2ChatBackendApplication.class, args);
    }

}
