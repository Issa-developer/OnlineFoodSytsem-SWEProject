package com.foodapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationBackendApplication.class, args);
        System.out.println("Authentication Backend is running...");
    }

}