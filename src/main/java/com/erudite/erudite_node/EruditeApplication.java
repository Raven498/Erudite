package com.erudite.erudite_node;

import com.erudite.erudite_node.management.Director;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		Director.direct();
	}

}
