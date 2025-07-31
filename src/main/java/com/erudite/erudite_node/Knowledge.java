package com.erudite.erudite_node;
//Could contain meta-knowledge
public class Knowledge implements Approximation {
    protected Agent.KClasses kclass;
    public String name;
    public String content;

    public Knowledge(String name, String content, Agent.KClasses kclass){
        this.name = name;
        this.content = content;
        this.kclass = kclass;
    }

    @Override
    public Knowledge approx(){
        return null;
    }
}
