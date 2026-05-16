package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DemoGoal extends Knowledge {
    public ArrayList<String> instances = new ArrayList<>();
    public ArrayList<String> attr_labels = new ArrayList<>();
    public ArrayList<Object> values = new ArrayList<>();
    public DemoGoal(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}
