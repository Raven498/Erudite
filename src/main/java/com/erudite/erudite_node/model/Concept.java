package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;

/*
TODO: Need to create a data type contract for each label that is inherited by Instance
 */
public class Concept extends Knowledge {
    public ArrayList<String> attr_labels;
    public ArrayList<String> behaviors;

    public Concept(String name, String content, Agent.KClasses kclass, ArrayList<String> attr_labels, ArrayList<String> behaviors) {
        super(name, content, kclass);
        this.attr_labels = attr_labels;
        this.behaviors = behaviors;
    }

    public String toString(){
        return String.format("%s{ATTRS: %s, BEHAVIORS: %s}", name, attr_labels, behaviors);
    }
}
