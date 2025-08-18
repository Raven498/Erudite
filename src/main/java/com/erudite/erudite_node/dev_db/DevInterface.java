package com.erudite.erudite_node.dev_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DB Interface for dev DB running on PostgreSQL
 */
@Component
public class DevInterface {
    @Autowired
    DevRepo devRepo;

    public List<Pod> getPods(){
        return devRepo.findAll();
    }

    public List<Pod> getPodsByIp(String ip){return devRepo.findByIp(ip);}
    public void addPod(String ip){
        Pod pod = new Pod();
        pod.setIp(ip);
        devRepo.save(pod);
    }
}
