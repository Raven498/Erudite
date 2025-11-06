package com.erudite.erudite_node.model;

public class GenState{
    public String prompt;
    public OldInstance f;

    public GenState(String prompt, OldInstance f){
        this.prompt = prompt;
        this.f = f;
    }
}
