package com.erudite.erudite_node.model;

//Turn into Generic to support more datatypes
public class OldInstance {
    public Concept c;
    public String[] values;

    public OldInstance(Concept c){
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
