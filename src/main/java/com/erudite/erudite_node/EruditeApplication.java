package com.erudite.erudite_node;

import com.erudite.erudite_node.management.Director;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {
	public static boolean delay = false;
	private static final int TOTAL_TIME_CAP = 60;
	private static final int DELAY_TIME = 1;

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		int startTime = getCurrentTime();
        ArrayList<Integer> roundTimes = new ArrayList<>();

		while(getCurrentTime() - startTime <= TOTAL_TIME_CAP){
            int roundStartTime = getCurrentTime();
			if(delay){
				delay();
			}
			Director.direct();
            int roundTime = getCurrentTime() - roundStartTime;
            roundTimes.add(roundTime);
            System.out.println("ROUND COMPLETION TIME FOR RESPONSE ABOVE: " + roundTime);
		}
		System.out.println("OVER");
        int totalSessionTime = 0;
        double avgRoundTime = 0.0;
        for(int r : roundTimes){
            totalSessionTime += r;
        }
        avgRoundTime = (double) totalSessionTime / roundTimes.size();
        System.out.println("AVG ROUND COMPLETION TIME: " + avgRoundTime);
        System.out.println("TOTAL SESSION TIME: " + totalSessionTime);
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
