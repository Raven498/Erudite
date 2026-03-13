package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DemoGoal extends Knowledge {
    public Map<String, String> attrs = new HashMap<>();
    public ArrayList<Object> values = new ArrayList<>();
    public DemoGoal(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}
