package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.Arrays;

//Turn into Generic to support more datatypes
public class InstanceKnowledge extends Knowledge{
    public Concept c;
    public Object[] values;

    public InstanceKnowledge(String name, String content, Agent.KClasses kclass, Concept c){
        super(name, content, kclass);
        this.c = c;
        this.values = new Object[c.attr_labels.size()];
    }

    public void addValue(String attr, Object v){
        values[c.attr_labels.indexOf(attr)] = v;
    }
    public Object getValue(String attr){
        return values[c.attr_labels.indexOf(attr)];
    }
    @Override
    public String toString() {
        return String.format("OF CONCEPT %s (aka %s), [%s], VALUES: %s", c.name, c.content, c, Arrays.toString(values));
    }

}
