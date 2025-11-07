package com.erudite.erudite_node.model;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private ArrayList<Knowledge> knowledge = new ArrayList<>();

    public ArrayList<Knowledge> getKnowledge(){
        return knowledge;
    }

    public void addKnowledge(Knowledge k){
        knowledge.add(k);
    }

    public void addKnowledge(List<Knowledge> k){
        knowledge.addAll(k);
    }

    public String toString(){
        return knowledge.toString();
    }
}
