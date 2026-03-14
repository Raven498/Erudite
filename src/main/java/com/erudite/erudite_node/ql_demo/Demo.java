package com.erudite.erudite_node.ql_demo;

import com.erudite.erudite_node.model.*;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Arrays;

public class Demo {
    static Concept c1 = new Concept("Pot", "C1", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("C2 color")));
    static Knowledge k1 = c1;
    static Concept c2 = new Concept("Color", "C2", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("str name", "double hue", "double value", "double sat")));
    static Knowledge k2 = c2;
    static InstanceKnowledge i1 = new InstanceKnowledge("Blue", "I1", Agent.KClasses.INSTANCE, c2);
    static Knowledge k3 = i1;
    static InstanceKnowledge i2 = new InstanceKnowledge("Red", "I2", Agent.KClasses.INSTANCE, c2);
    static Knowledge k4 = i2;
    static InstanceKnowledge i3 = new InstanceKnowledge("P1", "I3", Agent.KClasses.INSTANCE, c1);
    static Knowledge k5 = i3;
    static InstanceKnowledge i4 = new InstanceKnowledge("P2", "I4", Agent.KClasses.INSTANCE, c2);
    static Knowledge k6 = i4;
    static InstanceKnowledge i5 = new InstanceKnowledge("P3", "I5", Agent.KClasses.INSTANCE, c2);
    static Knowledge k7 = i5;
    static Concept c3 = new Concept("Current Pot", "C3", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("C1 pot")));
    static Knowledge k8 = c3;
    static InstanceKnowledge i6 = new InstanceKnowledge("CP1", "I6", Agent.KClasses.INSTANCE, c3);
    static Knowledge k9 = i6;
    static DemoGoal goal = new DemoGoal("Goal", "G1", Agent.KClasses.GOAL);

    public static void red_transition(){
        InstanceKnowledge pot = (InstanceKnowledge) (i6.getValue("C1 pot"));
        pot.addValue("C2 color", i2);
        if (i6.getValue("C1 pot") == i3) {
            i6.addValue("C1 pot", i4);
        } else if (i6.getValue("C1 pot") == i4) {
            i6.addValue("C1 pot", i5);
        } else if (i6.getValue("C1 pot") == i5) {
            i6.addValue("C1 pot", i4);
        }
    }

    public static void blue_transition(){
        InstanceKnowledge pot = (InstanceKnowledge) (i6.getValue("C1 pot"));
        pot.addValue("C2 color", i1);
        if (i6.getValue("C1 pot") == i3) {
            i6.addValue("C1 pot", i3);
        } else if (i6.getValue("C1 pot") == i4) {
            i6.addValue("C1 pot", i3);
        } else if (i6.getValue("C1 pot") == i5) {
            i6.addValue("C1 pot", i4);
        }
    }

    public static void main(String[] args){
        i3.addValue("C2 color", i1);

        i4.addValue("C2 color", i1);

        i5.addValue("C2 color", i1);

        i6.addValue("C1 pot", i3);

        goal.attrs.put("CP1.pot", "P3.color");
        goal.values.add(i5);
        goal.values.add(i1);
    }

}
