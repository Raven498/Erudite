package com.erudite.erudite_node;

import com.erudite.erudite_node.logging.Logger;
import com.erudite.erudite_node.management.Director;
import org.apache.juli.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.LocalTime;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {
	public static boolean delay = false;
	private static final int TOTAL_TIME_CAP = 2;
	private static final int DELAY_TIME = 1;

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		int startTime = getCurrentTime();
		int rounds = 1;
		while(getCurrentTime() - startTime < TOTAL_TIME_CAP){ // Director Mainloop
            if(rounds % 10 == 0){
                Logger.logIterationStart(rounds, getCurrentTime(), getCurrentTime() - startTime, delay);
            }
			if(delay){
				delay();
			}
			//Director.direct();
			rounds += 1;
		}
		Logger.saveLogs();
        System.out.println("OVER");
	}

	public static int getCurrentTime(){
		 return (LocalTime.now().getHour() * 60) + LocalTime.now().getMinute();
	}

	public static void delay(){
		int startTime = getCurrentTime();
		while(getCurrentTime() - startTime <= DELAY_TIME){
			Logger.logDelay(getCurrentTime(), getCurrentTime() - startTime, DELAY_TIME);
		}
	}

}
