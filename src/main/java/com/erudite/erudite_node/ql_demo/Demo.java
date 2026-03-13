package com.erudite.erudite_node.ql_demo;

import com.erudite.erudite_node.model.Concept;
import com.erudite.erudite_node.model.Instance;
import com.erudite.erudite_node.model.InstanceKnowledge;
import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Arrays;

public class Demo {
    Concept c1 = new Concept("Color", "C2", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("str name", "double hue", "double value", "double sat")));
    Knowledge k1 = new Concept("Pot", "C1", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("C2 color")));
    Concept c2 = new Concept("Color", "C2", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("str name", "double hue", "double value", "double sat")));
    Knowledge k2 = c2;
    Knowledge k3 = new InstanceKnowledge("Blue", "of C2", Agent.KClasses.INSTANCE, c2);

}
