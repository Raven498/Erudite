package com.erudite.erudite_node.model;

import java.util.ArrayList;
import java.util.List;

public class Environment {
    private List<Knowledge> knowledge = new ArrayList<>();

    public Environment() {

    }

    public Environment(List<Knowledge> knowledge){
        this.knowledge = knowledge;
    }

    public List<Knowledge> getKnowledge(){
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
