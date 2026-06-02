package com.erudite.erudite_node.ql_demo;

import com.erudite.erudite_node.model.*;
import com.erudite.erudite_node.service.Agent;

import java.lang.reflect.Array;
import java.util.*;

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
    static InstanceKnowledge i4 = new InstanceKnowledge("P2", "I4", Agent.KClasses.INSTANCE, c1);
    static Knowledge k6 = i4;
    static InstanceKnowledge i5 = new InstanceKnowledge("P3", "I5", Agent.KClasses.INSTANCE, c1);
    static Knowledge k7 = i5;
    static Concept c3 = new Concept("Current Pot", "C3", Agent.KClasses.CONCEPT,
            new ArrayList<>(Arrays.asList("C1 pot")));
    static Knowledge k8 = c3;
    static InstanceKnowledge i6 = new InstanceKnowledge("CP1", "I6", Agent.KClasses.INSTANCE, c3);
    static Knowledge k9 = i6;
    static DemoGoal goal = new DemoGoal("Goal", "G1", Agent.KClasses.GOAL);
    static Knowledge k12 = goal;
    static Environment e = new Environment();
    /*
    ITER 1:
    (Blue Trans): I3 color = blue --> I3 color = blue, I3 -> I3
    (Red Trans): I3 color = blue --> I3 color = red, I3 -> I4

    ITER 2:
    (Blue Trans): I4 color = blue --> I4 color = blue, I4 -> I3
    (Red Trans): I3 color = red --> I3 color = red, I3 -> I4

    ITER 3:
    (Blue Trans): I4 color = blue --> I4 color = blue, I4 -> I3
    (Red Trans): I3 color = red -> I3 color = red, I3 -> I4
    ...
     */
    public static void red_transition(){
        InstanceKnowledge pot = (InstanceKnowledge) (i6.getValue("C1 pot"));
        pot.addValue("C2 color", i2);
        //System.out.println(pot.getValue("C2 color"));
        //System.out.println(((InstanceKnowledge) i6.getValue("C1 pot")).getValue("C2 color"));
        if (i6.getValue("C1 pot") == i3) {
            i6.addValue("C1 pot", i4);
        } else if (i6.getValue("C1 pot") == i4) {
            i6.addValue("C1 pot", i5);
        } else if (i6.getValue("C1 pot") == i5) {
            i6.addValue("C1 pot", i4);
        }
    }

    /*
    (Red): I3 -> I4
    (Blue): I4 -> I3
    (Red): I3 -> I4
    (Blue): I4 -> I3
     */
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

    public static boolean goalSatisfied(){
        int matches = 0;
        for (Knowledge k : e.getKnowledge()){
            if (k.getKClass() == Agent.KClasses.INSTANCE){
                InstanceKnowledge i = (InstanceKnowledge) k;
                if (goal.instances.contains(i.content)){
                    int index = goal.instances.indexOf(i.content);
                    if (i.getValue(goal.attr_labels.get(index)).equals(goal.values.get(index))){
                        matches += 1;
                    }
                }
            }
        }
        //System.out.println(matches);
        if (matches == goal.values.size()){
            goal.resolved = true;
            return true;
        }
        return false;
    }

    public static Environment snapshotEnv(){
        Environment copy = new Environment();
        for (Knowledge k : e.getKnowledge()){
            Knowledge k_copy = null;
            if (k.getKClass() == Agent.KClasses.INSTANCE) {
                InstanceKnowledge i = (InstanceKnowledge) k;
                k_copy = new InstanceKnowledge(k.name, k.content, k.getKClass(), i.c);
                var i_copy = (InstanceKnowledge) k_copy;
                for (int j = 0; j < i.values.length; j++) {
                    i_copy.addValue(i.c.attr_labels.get(j), i.values[j]);
                }
            } else {
                k_copy = k;
            }
            copy.getKnowledge().add(k_copy);
        }
        return copy;
    }

    private static void initSpecificVectorSpace(ArrayList<ArrayList<ArrayList<Object>>> value_space, ArrayList<Object[]> svs){
        // partially flatten given value space
        // this will remove the delta knowledge level from the value space, keeping the delta attr and value levels
        ArrayList<ArrayList<Object>> flatValueSpace = new ArrayList<>();
        for (ArrayList<ArrayList<Object>> k : value_space) {
            flatValueSpace.addAll(k);
        }

        // initialize svs with empty specific vectors
        for (ArrayList<Object> a : flatValueSpace) {
            svs.add(new Object[] {});
        }

        /*
        iteratively create new combinations of value space to derive specific vectors
         */
        ArrayList<Object[]> updatedVectors = new ArrayList<>();
        for (ArrayList<Object> values : flatValueSpace) {
            for (Object v : values) {
                for (Object[] s : svs) {
                    Object[] new_s = new Object[s.length + 1];
                    new_s[new_s.length - 1] = v;
                    updatedVectors.add(new_s);
                }
            }
            svs.clear();
            svs.addAll(updatedVectors);
            updatedVectors.clear();
        }
    }

    private static Object[] getCurrentSpecVector(Environment e_delta, ArrayList<ArrayList<String>> attr_space, int vectorSize) {
        Object[] currSpecVector = new Object[vectorSize];
        int currIndex = 0;
        for (int k = 0; k < e_delta.getKnowledge().size(); k++) {
            if (e_delta.getKnowledge().get(k).getKClass() == Agent.KClasses.INSTANCE) {
                InstanceKnowledge instance = (InstanceKnowledge) e_delta.getKnowledge().get(k);
                ArrayList<String> d_attr_set = attr_space.get(k);
                for (String delta_attr : d_attr_set) {
                    currSpecVector[currIndex] = instance.getValue(delta_attr);
                    currIndex += 1;
                }
            }
        }
        return currSpecVector;
    }

    private static int q_argmax(Object[] currState, ArrayList<double[]> qTable, ArrayList<Object[]> specificVectorSpace) {
        for (int s = 0; s < specificVectorSpace.size(); s++) {
            if (Arrays.equals(specificVectorSpace.get(s), currState)) {
                double[] qValues = qTable.get(s);
                if (qValues[0] >= qValues[1]) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args){
        i3.addValue("C2 color", i1); // I1 DEFAULT

        i4.addValue("C2 color", i1); // I1 DEFAULT

        i5.addValue("C2 color", i1);

        i6.addValue("C1 pot", i3);

        goal.instances.add("CP1");
        goal.instances.add("P3");
        goal.attr_labels.add("C1 pot");
        goal.attr_labels.add("C2 color");
        goal.values.add(i5);
        goal.values.add(i1);

        List<Knowledge> tkb = new ArrayList<> (Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9, k12));
        e.addKnowledge(tkb);
        int n = 100;
        Environment e_master = snapshotEnv();
        Environment e_init = e_master;
        Environment e_delta = new Environment();
        ArrayList<ArrayList<String>> attr_space = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Object>>> value_space = new ArrayList<>();
        /*
        META QL
         */
        // episodic loop (for every episode)
        for(int i = 0; i < n; i++) {
            System.out.println("AT EPISODE: " + i);
            // inner episodic loop (the actual episode)
            int z = 0;
            while (!goalSatisfied()) {
                if (z > 100) {
                    System.out.println("GOAL NOT SATISFIED");
                    break;
                }
                z += 1;
                Random random = new Random();
                int action = random.nextInt(2);
                if (action == 0) {
                    blue_transition();
                } else {
                    red_transition();
                }
                // for all knowledge in the env
                for(Knowledge k : e.getKnowledge()) {
                    // for every Instance
                    if (k.getKClass() == Agent.KClasses.INSTANCE) {
                        InstanceKnowledge instance = (InstanceKnowledge) k;
                        InstanceKnowledge i_o = null;
                        // getting the respective original instance for comparison purposes and change detection
                        for (Knowledge k_o : e_init.getKnowledge()) {
                            if (k_o.content.equals(k.content)) {
                                i_o = (InstanceKnowledge) k_o;
                            }
                        }

                        if (i_o == null) {
                            break;
                        }
                        // for each attribute of this Instance
                        for (String attr : instance.c.attr_labels) {
                                /*
                                Instance's attribute value has changed from last iteration
                                 */
                            if (!Objects.equals(instance.getValue(attr), i_o.getValue(attr))) {
                                int k_index = e_delta.getKnowledge().indexOf(k);
                                // if this Instance has NOT already been detected as a delta in a previous iteration/episode
                                // This is for new delta Instances, so no need to track pre-existence of delta attrs/values (they won't already exist)
                                if (!e_delta.getKnowledge().contains(k)) {
                                    // add as NEW delta Instance
                                    e_delta.addKnowledge(k);
                                    // add attr as NEW delta attr
                                    attr_space.add(new ArrayList<>(Arrays.asList(attr)));
                                    // add value as NEW delta value
                                    value_space.add(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(instance.getValue(attr))))));
                                }
                                // if this Instance HAS already been detected as a delta in a previous iteration/episode
                                // (even if a Instance is already a delta, a new delta attr of that Instance could've still been found)
                                // (AKA one delta Instance can have multiple delta attrs)
                                // this else statement processes this case
                                else {
                                    int a_index = attr_space.get(k_index).indexOf(attr);
                                    // if the attr space does NOT already contain this attr as a delta attr for this particular delta Instance
                                    if (!attr_space.get(k_index).contains(attr)) {
                                        // add attr as NEW delta attr
                                        attr_space.get(k_index).add(attr);
                                        // add value as NEW delta value for new delta attr in this particular delta Instance
                                        value_space.get(k_index).add(new ArrayList<>(Arrays.asList(instance.getValue(attr))));
                                    }
                                    // if the attr space ALREADY contains this attr as a delta attr for this particular delta Instance
                                    else {
                                        // if the value space does NOT already contain this value of this delta attr for this particular delta Instance
                                        if (!value_space.get(k_index).get(a_index).contains(instance.getValue(attr))) {
                                            // add NEW delta value to this delta attr for this particular delta Instance
                                            value_space.get(k_index).get(a_index).add(instance.getValue(attr));
                                        }
                                        // if the attr space ALREADY contains this attr as a delta attr for this particular delta Instance
                                        // (don't have to do anything since the value is already there)
                                    }
                                }

                            } /*else if (e_delta.getKnowledge().contains(k)) {
                                    boolean inValueSpace = false;
                                    for (Object v : value_space.get(e_delta.getKnowledge().indexOf(k))) {
                                        if (v.equals(instance.getValue(attr))) {
                                            inValueSpace = true;
                                        }
                                    }

                                    if (!inValueSpace) {
                                        value_space.get(e_delta.getKnowledge().indexOf(k)).add(instance.getValue(attr));
                                    }
                                }*/
                        }
                    }
                }
                e_init = snapshotEnv();
                /*
                if (z < 5) {
                    Logger.envReport(e, new ArrayList<>(Arrays.asList("I6")));
                    System.out.println(((InstanceKnowledge) i6.getValue("C1 pot")).getValue("C2 color"));
                    Logger.printLogs();
                    Logger.flushLogs();
                    z += 1;
                }

                 */
            }
            e_init = e_master;
        }

        System.out.println("-------------------- ENVIRONMENT DELTAS -----------------------");
        for (int i = 0; i < e_delta.getKnowledge().size(); i++) {
            for (int j = 0; j < attr_space.get(i).size(); j++) {
                for (Object v : value_space.get(i).get(j)) {
                    if (v instanceof InstanceKnowledge) {
                        System.out.println(e_delta.getKnowledge().get(i).content + ": " + attr_space.get(i).get(j) + ": " + ((InstanceKnowledge) v).content);
                    } else {
                        System.out.println(e_delta.getKnowledge().get(i).content + ": " + attr_space.get(i).get(j) + ": " + v);
                    }
                }
            }
        }

        /*
        PRACTICAL QL
         */
        ArrayList<Object[]> specificVectors = new ArrayList<>();
        ArrayList<double[]> qTable = new ArrayList<>();
        int EPISODE_THRESHOLD = 1000;
        double EPSILON_MAX = 1.0;
        double EPSILON_MIN = 0.05;
        double epsilon = EPSILON_MAX;
        double ALPHA = 0.7;
        double GAMMA = 0.95;
        double EPSILON_DECAY = 0.0005;

        /*
        init specific vector space
         */
        initSpecificVectorSpace(value_space, specificVectors);

        /*
        init q table
        format for each entry: {blueQ, redQ}
         */
        for (Object[] ignored : specificVectors) {
            qTable.add(new double[] {0.0, 0.0});
        }

        for (int i = 0; i < EPISODE_THRESHOLD; i++) {
            epsilon = EPSILON_MIN + Math.pow((EPSILON_MAX - EPSILON_MIN), -(ALPHA * i));
            e = e_master;
            while (!goalSatisfied()) {
                /*
                epsilon-greedy
                 */
                Random random = new Random();
                if (random.nextDouble() < epsilon) {
                    // exploration
                    if (random.nextInt(2) == 0) {
                        blue_transition();
                    } else {
                        red_transition();
                    }
                } else {
                    // exploitation (use argmax)
                    /*
                    Construct current specific vector by finding current values of delta attrs
                    Specific vectors are equivalent to states in practical QL, so this current spec vector is the current state
                     */
                    Object[] currState = getCurrentSpecVector(e_delta, attr_space, specificVectors.getFirst().length);

                    // conduct argmax using current state
                    int actionID = q_argmax(currState, qTable, specificVectors);
                    // TODO: Implement all action execution handling in general in ActionController
                    if (actionID == 0) {
                        blue_transition();
                    } else if (actionID == -1) {
                        System.out.println("Could not find the current state in specific vector space");
                        return;
                    } else {
                        red_transition();
                    }
                }

            }
        }
    }

}
