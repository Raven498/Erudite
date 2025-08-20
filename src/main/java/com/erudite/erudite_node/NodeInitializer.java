package com.erudite.erudite_node;

import com.erudite.erudite_node.dev_db.DevInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NodeInitializer {
    @Autowired
    DevInterface devInterface;
    String ip = "192.168.1.2";

    @EventListener
    public void registerNode(ApplicationReadyEvent event){
        // USE ONLY WITH K8S DEPLOYMENT
        //ip = System.getenv("POD_IP");
        if(devInterface.getPodsByIp(ip).isEmpty()){
            // Write to db
            devInterface.addPod(ip);
        }
    }
}
