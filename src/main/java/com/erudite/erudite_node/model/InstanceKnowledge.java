package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

//Turn into Generic to support more datatypes
public class InstanceKnowledge extends Knowledge{
    public Concept c;
    public String[] values;

    public InstanceKnowledge(String name, String content, Agent.KClasses kclass, Concept c){
        super(name, content, kclass);
        this.c = c;
        this.values = new String[c.attr_labels.size()];
    }

    public void addValue(String attr, String v){
        values[c.attr_labels.indexOf(attr)] = v;
    }
    public String getValue(String attr){
        return values[c.attr_labels.indexOf(attr)];
    }

}
