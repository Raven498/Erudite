package com.erudite.erudite_node.dev_db;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * DB Interface for dev DB running on PostgreSQL
 */
public class DevInterface {
    @Autowired
    DevRepo devRepo;

    public List<Pod> getPods(){
        return devRepo.findAll();
    }
}
