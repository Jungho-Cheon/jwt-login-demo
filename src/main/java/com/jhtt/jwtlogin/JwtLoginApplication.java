package com.jhtt.jwtlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class JwtLoginApplication {

	private final static String PROPERTIES = "spring.config.location=classpath:/application.yml,classpath:jwt-secret.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(JwtLoginApplication.class)
				.properties(PROPERTIES)
				.run();
	}

}
