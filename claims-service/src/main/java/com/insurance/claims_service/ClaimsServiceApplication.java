package com.insurance.claims_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ClaimsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClaimsServiceApplication.class, args);
	}

}
