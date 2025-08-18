package com.erudite.erudite_node;

import com.erudite.erudite_node.dev_db.DevInterface;
import com.erudite.erudite_node.management.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.erudite.erudite_node")
public class EruditeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EruditeApplication.class, args);
		DevInterface devInterface = new DevInterface();
		if(devInterface.getPodsByIp("192.168.1.2").isEmpty()){
			// Write to db
			devInterface.addPod("192.168.1.2");
		}
		// Director.direct();
	}

}
