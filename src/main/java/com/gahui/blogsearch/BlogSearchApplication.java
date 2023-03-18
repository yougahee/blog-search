package com.gahui.blogsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BlogSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogSearchApplication.class, args);
	}

}
