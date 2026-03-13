package com.erudite.erudite_node.ql_demo;

import com.erudite.erudite_node.model.Concept;
import com.erudite.erudite_node.model.Instance;
import com.erudite.erudite_node.model.InstanceKnowledge;
import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Arrays;

public class Demo {
    public static void main(String[] args){
        Concept c1 = new Concept("Pot", "C1", Agent.KClasses.CONCEPT,
                new ArrayList<>(Arrays.asList("C2 color")));
        Knowledge k1 = c1;
        Concept c2 = new Concept("Color", "C2", Agent.KClasses.CONCEPT,
                new ArrayList<>(Arrays.asList("str name", "double hue", "double value", "double sat")));
        Knowledge k2 = c2;
        InstanceKnowledge i1 = new InstanceKnowledge("Blue", "I1", Agent.KClasses.INSTANCE, c2);
        Knowledge k3 = i1;
        InstanceKnowledge i2 = new InstanceKnowledge("Red", "I2", Agent.KClasses.INSTANCE, c2);
        Knowledge k4 = i2;
        InstanceKnowledge i3 = new InstanceKnowledge("P1", "I3", Agent.KClasses.INSTANCE, c1);
        i3.addValue("C2 color", i1);
        Knowledge k5 = i3;
        InstanceKnowledge i4 = new InstanceKnowledge("P2", "I4", Agent.KClasses.INSTANCE, c2);
        i4.addValue("C2 color", i1);
        Knowledge k6 = i4;
        InstanceKnowledge i5 = new InstanceKnowledge("P3", "I5", Agent.KClasses.INSTANCE, c2);
        i5.addValue("C2 color", i1);
        Knowledge k7 = i5;
        Concept c3 = new Concept("Current Pot", "C3", Agent.KClasses.CONCEPT,
                new ArrayList<>(Arrays.asList("C1 pot")));
        Knowledge k8 = c3;
        InstanceKnowledge i6 = new InstanceKnowledge("CP1", "I6", Agent.KClasses.INSTANCE, c3);
        i6.addValue("C1 pot", i3);
        Knowledge k9 = i6;
        // GOAL DEF HERE
    }

}
