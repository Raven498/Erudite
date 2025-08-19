package com.erudite.erudite_node;

import com.erudite.erudite_node.dev_db.DevInterface;
import com.erudite.erudite_node.dev_db.DevRepo;
import com.erudite.erudite_node.management.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {
    @Autowired
    DevInterface devInterface;

    @GetMapping("/algoInfo")
    public AlgoInfo getAlgoInfo(){
        return new AlgoInfo("POWER RULE", "O.a = I.a * I.n, O.x = I.x, O.n = I.n - 1");
    }

    @GetMapping("/instanceTest")
    public void instanceTest() throws IOException {
        System.out.println(Director.getTrueInstance());
    }

    @GetMapping("/getPods")
    public void getPods(){
        System.out.println(devInterface.getPods());
    }

    /**
     * This endpoint should only be called by the Kubernetes deployment if it decides to destroy this pod,
     * via the preStop hook.
     * Removes pod ip from the registry to handle pod removals
     */
    @GetMapping("/destroy")
    public void destroy(){
        String ip = System.getenv("POD_IP");
        if(!devInterface.getPodsByIp(ip).isEmpty()){
            // Delete from db
            devInterface.deletePod(ip);
        }
    }
}