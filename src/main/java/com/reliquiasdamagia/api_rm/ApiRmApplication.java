package com.reliquiasdamagia.api_rm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.reliquiasdamagia.api_rm"})
public class ApiRmApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRmApplication.class, args);
	}

}
