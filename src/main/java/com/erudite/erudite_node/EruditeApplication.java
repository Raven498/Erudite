package com.erudite.erudite_node;

import com.erudite.erudite_node.management.Director;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalTime;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		int startTime = getCurrentTime();
		while(getCurrentTime() - startTime <= 2){
			Director.direct();
		}
	}

	public static int getCurrentTime(){
		 return (LocalTime.now().getHour() * 60) + LocalTime.now().getMinute();
	}

}
