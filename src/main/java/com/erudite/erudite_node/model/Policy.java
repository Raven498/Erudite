package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

public class Policy extends Knowledge {
    public Policy(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}

