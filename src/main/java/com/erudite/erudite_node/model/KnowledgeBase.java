package com.erudite.erudite_node.model;

import com.erudite.erudite_node.model.Environment;
import com.erudite.erudite_node.service.Agent;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;

import java.util.*;

public class KnowledgeBase {
    private Map<UUID, Environment> env_set = new HashMap<>();
    private double[] currentTypeDistribution  = new double[Agent.kTypes];

    public void addEnv(UUID uuid, Environment env) {
        env_set.putIfAbsent(uuid, env);
    }
    public Environment getEnv(UUID uuid){
        return env_set.getOrDefault(uuid, new Environment());
    }

    public Map<UUID, Environment> getEnvSet() {
        return env_set;
    }

    public void setEnvSet(Map<UUID, Environment> env_set){
        this.env_set = env_set;
    }
    public void setEnvSet(List<Environment> env_set){

    }

    public String toString(){
        return getEnvSet().toString();
    }

    public double[] getCurrentTypeDistribution(){
        return currentTypeDistribution;
    }

    public void setCurrentTypeDistribution(){this.currentTypeDistribution = currentTypeDistribution;}

    public void setCurrentTypeDistribution(double[] currentTypeDistribution){
        this.currentTypeDistribution = currentTypeDistribution;
    }
}