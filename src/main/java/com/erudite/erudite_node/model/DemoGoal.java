package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DemoGoal extends Knowledge {
    public ArrayList<InstanceKnowledge> instances = new ArrayList<>();
    public ArrayList<String> attr_labels = new ArrayList<>();
    public ArrayList<Object> values = new ArrayList<>();
    public boolean resolved = false;
    public DemoGoal(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
    @Override
    public String toString() {
        return String.format("");
    }

}
