package com.example.openapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

@SpringBootApplication
public class OpenApiApplication {

	public static void main(final String[] args) {
		SpringApplication.run(OpenApiApplication.class, args);
	}

	@Bean
	public ProblemModule problemModule() {
		return new ProblemModule();
	}

	@Bean
	public ConstraintViolationProblemModule constraintViolationProblemModule() {
		return new ConstraintViolationProblemModule();
	}
}
