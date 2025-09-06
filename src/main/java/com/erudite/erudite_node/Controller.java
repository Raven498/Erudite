package com.erudite.erudite_node;

import com.erudite.erudite_node.dev_db.DevInterface;
import com.erudite.erudite_node.dev_db.DevRepo;
import com.erudite.erudite_node.dev_db.Pod;
import com.erudite.erudite_node.management.Director;
import com.erudite.erudite_node.model.InstanceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import static okhttp3.RequestBody.*;

import org.apache.catalina.core.ApplicationContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.web.bind.annotation.RequestBody.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {
    @Autowired
    DevInterface devInterface;

    int propCycle = 0;

    @Autowired
    NodeInitializer nodeInit;

    @GetMapping("/algoInfo")
    public AlgoInfo getAlgoInfo(){
        return new AlgoInfo("POWER RULE", "O.a = I.a * I.n, O.x = I.x, O.n = I.n - 1");
    }

    @GetMapping("/instanceTest")
    public void instanceTest() throws IOException {
        InstanceTest instance = Director.getTrueInstance();
        //propagate(instance, true);
        System.out.println(instance);
        System.out.println("IP FOR THIS MACHINE: " + InetAddress.getLocalHost().getHostAddress());
    }

    // UNTESTED
    @PostMapping("/propagate/{isApproxNode}")
    public void propagate(@RequestBody InstanceTest instance, @PathVariable("isApproxNode") boolean isApproxNode) throws IOException {
        System.out.println("INSTANCE INPUT FOR THIS PROPAGATE: " + instance); // TODO: print the ip here somehow
        System.out.println("IS APPROX NODE: " + isApproxNode);
        System.out.println("PROP CYCLE: " + propCycle);
        if(propCycle < 1){
            List<Pod> pods = devInterface.getPods();
            // CONDUCT APPROXIMATION
            instance.attrs().put("APPROXED", "true");
            for(Pod pod : pods){
                if(Objects.equals(nodeInit.ip, pod.getIp())){
                    continue;
                }
                OkHttpClient client = new OkHttpClient.Builder().writeTimeout(20, TimeUnit.SECONDS).build();
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(instance);
                Request request = new Request.Builder()
                        .url("http://" + pod.getIp() + ":8081/propagate/false")
                        .addHeader("Content-Type", "application/json")
                        .post(create(json, MediaType.get("application/json")))
                        .build();

                client.newCall(request).execute();
            }
            propCycle += 1;
        } else{
            System.out.println("CYCLE LIMIT REACHED"); // TODO: print the ip here somehow
            if(isApproxNode){
                // TODO: Persist approx to DB
                System.out.println("IS APPROX NODE!!!");
            }
        }
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
    @GetMapping("/destroy/{ip}")
    public void destroy(@PathVariable("ip") String ip){
        // USE ONLY WITH K8S DEPLOYMENT
        //String ip = System.getenv("POD_IP");
        if(!devInterface.getPodsByIp(ip).isEmpty()){
            // Delete from db
            devInterface.deletePod(ip);
        }
    }
}