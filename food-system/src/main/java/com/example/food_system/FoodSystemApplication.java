package com.example.food_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.food_system")
public class FoodSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodSystemApplication.class, args);
	}
}
