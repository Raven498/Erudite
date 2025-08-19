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

    @EventListener
    public void registerNode(ApplicationReadyEvent event){
        if(devInterface.getPodsByIp("192.168.1.3").isEmpty()){
            // Write to db
            devInterface.addPod("192.168.1.3");
        }
    }
}
