package com.example.batteries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BatteriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatteriesApplication.class, args);
	}

}
