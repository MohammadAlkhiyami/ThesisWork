package com.learnreactivespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@SpringBootApplication
@EnableEurekaClient
public class LearnReactivespringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnReactivespringApplication.class, args);
	}
}
