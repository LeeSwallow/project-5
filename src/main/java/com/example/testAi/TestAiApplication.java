package com.example.testAi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TestAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestAiApplication.class, args);
	}

}
