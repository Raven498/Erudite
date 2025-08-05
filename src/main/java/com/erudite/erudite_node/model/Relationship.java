package com.erudite.erudite_node.model;
import com.erudite.erudite_node.service.Agent;

//Currently only models univariate relationships

public class Relationship extends Knowledge {
    private double x;
    private double w;
    private double b;

    public Relationship(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}