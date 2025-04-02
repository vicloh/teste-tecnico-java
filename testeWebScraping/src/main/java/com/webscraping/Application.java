package com.webscraping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.webscraping")
@ComponentScan(basePackages = "com.webscraping")
public class Application {
	public static void main(String[] args) {SpringApplication.run(Application.class, args);

	}
}

