package com.erudite.erudite_node;

import com.erudite.erudite_node.management.Director;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalTime;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {
	public static boolean delay = false;
	private static final int TOTAL_TIME_CAP = 10;
	private static final int DELAY_TIME = 1;

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		int startTime = getCurrentTime();
		while(getCurrentTime() - startTime <= TOTAL_TIME_CAP){
			if(delay){
				delay();
			}
			Director.direct();
		}
		System.out.println("OVER");
	}

	public static int getCurrentTime(){
		 return (LocalTime.now().getHour() * 60) + LocalTime.now().getMinute();
	}

	public static void delay(){
		int startTime = getCurrentTime();
		while(getCurrentTime() - startTime <= DELAY_TIME){

		}
	}

}
