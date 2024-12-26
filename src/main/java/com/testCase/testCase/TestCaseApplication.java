package com.testCase.testCase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestCaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestCaseApplication.class, args);
	}

}
