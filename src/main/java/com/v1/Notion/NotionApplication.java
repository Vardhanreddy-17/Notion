package com.v1.Notion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.v1.Notion"})
public class NotionApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotionApplication.class, args);
	}

}
