package com.erudite.erudite_node.model;

public class GenState{
    public String prompt;
    public Instance f;

    public GenState(String prompt, Instance f){
        this.prompt = prompt;
        this.f = f;
    }
}
