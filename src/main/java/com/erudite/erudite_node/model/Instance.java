package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Map;

public class Instance extends Knowledge {
    String instanceName;
    String conceptName;
    Map<String, String> attrs;
    ArrayList<String> behaviorNames;

    public void convertFromTest(InstanceTest instanceTest){
        this.instanceName = instanceTest.objectName();
        super.name = instanceName;
        this.conceptName = instanceTest.className();
        this.attrs = instanceTest.attrs();
        this.behaviorNames = instanceTest.behaviorNames();
    }

    public Instance(){
        super("", "[ATTR-VALUES GO HERE]", Agent.KClasses.INSTANCE);
    }
}