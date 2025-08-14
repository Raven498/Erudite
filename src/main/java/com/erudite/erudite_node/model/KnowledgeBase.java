package com.erudite.erudite_node.model;

import com.erudite.erudite_node.model.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KnowledgeBase {
    private Map<UUID, Environment> env_set;

    public void addEnv(UUID uuid, Environment env) {
        env_set.putIfAbsent(uuid, env);
    }

    public Map<UUID, Environment> getEnvSet() {
        return env_set;
    }

    public void setEnvSet(Map<UUID, Environment> env_set){
        this.env_set = env_set;
    }
    public void setEnvSet(List<Environment> env_set){

    }
}