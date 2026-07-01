package com.erudite.erudite_node.ql_demo;

import com.erudite.erudite_node.model.*;
import com.erudite.erudite_node.service.Agent;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.lang.reflect.Array;
import java.util.*;

/*
 * This demo experiments with the Meta QL algorithm, which works to identify the aspects of an environment most likely to
 * contribute to resolution of the environment by satisfying the goal (or reaching the terminal state). The inital version (V0)
 * of this algorithm does this by stepping through the environment with randomly chosen actions for n episodes and detecting all environmental instances
 * that changes after each time step. It stores them in another environment called the delta env, their delta attributes (the respective instances' attrs that
 * change in response to random actions) in the attribute space, and their delta values (all possible values that delta attributes may change to in the env)
 * in the value space.
 *
 * This demo also demonstrates another algorithm called Practical QL, which is mostly similar to regular Q learning but represents the states of its
 * environment using the outputs of Meta QL. It builds the specific vector space (SVS), which is a set of vectors that represents all possible combinations
 * of all values of all delta attrs (these vectors are called specific vectors). This SVS serves as the state space for Practical QL, with the specific vectors
 * themselves being the states. From there, Practical QL conducts regular Q learning over all these vectors against all possible actions, and constructs the
 * final Q table which, when followed, will hopefully result in environment resolution.
 *
 * This demo currently contains initial implementations of both algorithms along with a basic eval loop, but it requires many more
 * overall functionality, testing, efficiency, and readability changes. All outstanding required changes are listed here in the form of TODOs:
 * --> TODO: Refactor all action execution logic into ActionController
 * --> TODO: Generalize demo to include any number of actions by including and processing them as environmental knowledge
 * --> TODO: Create a better way to visualize resulting Q table from Practical QL
 * --> TODO: Refactor Meta, Practical QL algorithms into their own methods
 * --> TODO: Ensure env resets are happening correctly at the right time in both QL algorithms
 * --> TODO: Find ways to refactor and generalize QL algorithms and logic into separate and modular classes
 * --> TODO: Figure out better terminating conditions for Meta QL loop
 */
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

    public static void red_transition(){
        //System.out.println("RED TRANS");
        InstanceKnowledge i6_ref = (InstanceKnowledge) e.getKnowledge("I6");
        InstanceKnowledge i3_ref = (InstanceKnowledge) e.getKnowledge("I3");
        InstanceKnowledge i4_ref = (InstanceKnowledge) e.getKnowledge("I4");
        InstanceKnowledge i5_ref = (InstanceKnowledge) e.getKnowledge("I5");

        InstanceKnowledge pot = (InstanceKnowledge) (i6_ref.getValue("C1 pot"));
        pot.addValue("C2 color", i2);
        //System.out.println(pot.getValue("C2 color"));
        //System.out.println(((InstanceKnowledge) i6.getValue("C1 pot")).getValue("C2 color"));
        if (i6_ref.getValue("C1 pot") == i3_ref) {
            i6_ref.addValue("C1 pot", i4_ref);
        } else if (i6_ref.getValue("C1 pot") == i4_ref) {
            //System.out.println("SWITCHING TO I5 - RED");
            i6_ref.addValue("C1 pot", i5_ref);
        } else if (i6_ref.getValue("C1 pot") == i5_ref) {
            //System.out.println("AN I-5 HAS BEEN FILED - RED");
            i6_ref.addValue("C1 pot", i4_ref);
        }
    }


    public static void blue_transition(){
        //System.out.println("BLUE TRANS");
        InstanceKnowledge i6_ref = (InstanceKnowledge) e.getKnowledge("I6");
        InstanceKnowledge i3_ref = (InstanceKnowledge) e.getKnowledge("I3");
        InstanceKnowledge i4_ref = (InstanceKnowledge) e.getKnowledge("I4");
        InstanceKnowledge i5_ref = (InstanceKnowledge) e.getKnowledge("I5");
        InstanceKnowledge pot = (InstanceKnowledge) (i6_ref.getValue("C1 pot"));
        pot.addValue("C2 color", i1);
        if (i6.getValue("C1 pot") == i3_ref) {
            i6.addValue("C1 pot", i3_ref);
        } else if (i6.getValue("C1 pot") == i4_ref) {
            i6.addValue("C1 pot", i3_ref);
        } else if (i6.getValue("C1 pot") == i5_ref) {
            //System.out.println("AN I-5 HAS BEEN FILED- BLUE");
            i6.addValue("C1 pot", i4_ref);
        }
    }

    public static boolean goalSatisfied(){
        int matches = 0;
        DemoGoal goal = (DemoGoal) e.getKnowledge("G1");
        for (int i = 0; i < goal.instances.size(); i++) {
            InstanceKnowledge e_instance = (InstanceKnowledge) (e.getKnowledge(goal.instances.get(i).content));
            if (e_instance.getValue(goal.attr_labels.get(i)) == goal.values.get(i)) {
                matches += 1;
            }
        }
        if (matches == goal.values.size()){
            goal.resolved = true;
            return true;
        }
        return false;
    }

    public static Environment snapshotEnv(Environment env){
        Environment copy = new Environment();
        for (Knowledge k : env.getKnowledge()){
            Knowledge k_copy = null;
            if (k.getKClass() == Agent.KClasses.INSTANCE) {
                InstanceKnowledge i = (InstanceKnowledge) k;
                k_copy = new InstanceKnowledge(k.name, k.content, k.getKClass(), i.c);
                var i_copy = (InstanceKnowledge) k_copy;
                for (int j = 0; j < i.values.length; j++) {
                    i_copy.addValue(i.c.attr_labels.get(j), i.values[j]);
                }
            }
            else {
                k_copy = k;
            }
            copy.getKnowledge().add(k_copy);
        }

        for (int j = 0; j < copy.getKnowledge().size(); j++) {
            Knowledge k_copy = null;
            Knowledge k = copy.getKnowledge().get(j);
            if (k.getKClass() == Agent.KClasses.GOAL) {
                DemoGoal g = (DemoGoal) k;
                DemoGoal g_copy = new DemoGoal(g.name, g.content, Agent.KClasses.GOAL);
                for (InstanceKnowledge i : g.instances) {
                    InstanceKnowledge i_copy = (InstanceKnowledge) (copy.getKnowledge(i.content));
                    g_copy.instances.add(i_copy);
                }
                g_copy.attr_labels = g.attr_labels;
                g_copy.values = g.values;
                g_copy.resolved = g.resolved;
                k_copy = g_copy;
                copy.getKnowledge().set(j, k_copy);
            }
        }
        return copy;
    }

    /*
    TODO: When initializing the SVS, it produces duplicates of each specific vector
    The number of duplicates for each vector seems to correspond with the number of values in each vector
     */
    private static void initSpecificVectorSpace(ArrayList<ArrayList<ArrayList<Object>>> value_space, ArrayList<Object[]> svs){
        // partially flatten given value space
        // this will remove the delta knowledge level from the value space, keeping the delta attr and value levels
        System.out.println("INIT SVS");
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
                //System.out.println(v.toString());
                for (Object[] s : svs) {
                    Object[] new_s = new Object[s.length + 1];
                    System.arraycopy(s, 0, new_s, 0, s.length);
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

    private static int svsIndexOf(ArrayList<Object[]> specificVectorSpace, Object[] specificVector) {
        for (int i = 0; i < specificVectorSpace.size(); i++) {
            /*
            for (Object o : specificVectorSpace.get(i)) {
                System.out.print(((Knowledge) o).content + ",");
            }
             */
            System.out.println("SVS VECTOR: " + Arrays.toString(convertToIDVector(specificVectorSpace.get(i))));
            if (Arrays.equals(specificVectorSpace.get(i), specificVector)) {
                return i;
            }
            //System.out.println();
        }
        return -1;
    }

    private static double q_max(Object[] currState, ArrayList<double[]> qTable, ArrayList<Object[]> specificVectorSpace) {
        int actionID = q_argmax(currState, qTable, specificVectorSpace);
        System.out.println(actionID);
        System.out.println("CURR STATE: " + Arrays.toString(convertToIDVector(currState)));
        return qTable.get(svsIndexOf(specificVectorSpace, currState))[actionID]; //svsIndexOf is -1
    }

    private static String[] convertToIDVector(Object[] stateVector) {
        String[] idVector = new String[stateVector.length];
        for (int i = 0; i < stateVector.length; i++) {
            idVector[i] = ((Knowledge) (stateVector[i])).content;
        }
        return idVector;
    }

    /*
    version 1 of the reward function
    returns reward only in the terminal state
     */
    private static double rewardFuncV1() {
        if (goalSatisfied()) {
            return 1.0;
        }
        return 0.0;
    }

    public static void main(String[] args){
        i3.addValue("C2 color", i1); // I1 DEFAULT

        i4.addValue("C2 color", i1); // I1 DEFAULT

        i5.addValue("C2 color", i1);

        i6.addValue("C1 pot", i3);

        goal.instances.add(i6);
        goal.instances.add(i5);
        /*
        goal:
        CP1.pot = I5 (P3)
        P3.color = I1 (Blue)
         */
        goal.attr_labels.add("C1 pot");
        goal.attr_labels.add("C2 color");
        goal.values.add(i5);
        goal.values.add(i1);

        List<Knowledge> tkb = new ArrayList<> (Arrays.asList(k1, k2, k3, k4, k5, k6, k7, k8, k9, k12));
        e.addKnowledge(tkb);
        int n = 100;
        Environment e_master = snapshotEnv(e);
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
                System.out.println("STARTING EPISODE");
                if (z > 500) {
                    System.out.println("GOAL NOT SATISFIED");
                    break;
                }
                z += 1;
                Random random = new Random();
                int action = random.nextInt(2);
                if (action == 0) {
                    //System.out.println("BLUE TRANS");
                    blue_transition();
                } else {
                    //System.out.println("RED TRANS");
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
                            System.out.println("COULD NOT FIND ORIGINAL INSTANCE");
                            break;
                        }

                        // for each attribute of this Instance
                        for (String attr : instance.c.attr_labels) {
                                /*
                                Instance's attribute value has changed from last iteration
                                 */
                            if (!Objects.equals(instance.getValue(attr), i_o.getValue(attr))) {
                                System.out.println("INSTANCE VALUE CHANGED");
                                System.out.println(instance.content);
                                if (instance.content.equals("I5")) {
                                    System.out.println("AN I5 ATTR HAS CHANGED");
                                    System.out.println("I5 ATTR: " + attr);
                                }
                                int k_index = e_delta.getKnowledge().indexOf(k);
                                // if this Instance has NOT already been detected as a delta in a previous iteration/episode
                                // This is for new delta Instances, so no need to track pre-existence of delta attrs/values (they won't already exist)
                                if (!e_delta.getKnowledge().contains(k)) {
                                    if (instance.content.equals("I5")) {
                                        System.out.println("I5 NOT ALREADY REGISTERED");
                                        System.out.println("I5 ATTR BEING REGISTERED: " + attr);
                                        System.out.println("I5 VALUE BEING REGISTERED: " + ((InstanceKnowledge) (instance.getValue(attr))).content);
                                    }
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
                                    if (instance.content.equals("I5")) {
                                        System.out.println("I5 ALREADY REGISTERED");
                                    }
                                    int a_index = attr_space.get(k_index).indexOf(attr);
                                    // if the attr space does NOT already contain this attr as a delta attr for this particular delta Instance
                                    if (!attr_space.get(k_index).contains(attr)) {
                                        if (instance.content.equals("I5")) {
                                            System.out.println("I5 ATTR NOT ALREADY REGISTERED: " + attr);
                                            System.out.println("I5 VALUE NOT ALREADY REGISTERED: " + ((InstanceKnowledge) (instance.getValue(attr))).content);
                                        }
                                        // add attr as NEW delta attr
                                        attr_space.get(k_index).add(attr);
                                        // add value as NEW delta value for new delta attr in this particular delta Instance
                                        value_space.get(k_index).add(new ArrayList<>(Arrays.asList(instance.getValue(attr))));
                                    }
                                    // if the attr space ALREADY contains this attr as a delta attr for this particular delta Instance
                                    else {
                                        if (instance.content.equals("I5")) {
                                            System.out.println("I5 ATTR ALREADY REGISTERED: " + attr);
                                        }
                                        // if the value space does NOT already contain this value of this delta attr for this particular delta Instance
                                        if (!value_space.get(k_index).get(a_index).contains(instance.getValue(attr))) {
                                            if (instance.content.equals("I5")) {
                                                System.out.println("I5 VALUE NOT ALREADY REGISTERED: " + ((InstanceKnowledge) (instance.getValue(attr))).content);
                                            }
                                            // add NEW delta value to this delta attr for this particular delta Instance
                                            value_space.get(k_index).get(a_index).add(instance.getValue(attr));
                                        }
                                        // if the attr space ALREADY contains this attr as a delta attr for this particular delta Instance
                                        // (don't have to do anything since the value is already there)
                                        if (instance.content.equals("I5")) {
                                            System.out.println("I5 VALUE ALREADY REGISTERED: " + ((InstanceKnowledge) (instance.getValue(attr))).content);
                                            System.out.println("NOTHING TO DO");
                                        }
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
                            else {
                                /*
                                System.out.println("NOT CHANGED");
                                System.out.println("INSTANCE: " + instance.content);
                                System.out.println("INSTANCE ATTR: " + attr);
                                System.out.println("INSTANCE ATTR VAL: " + instance.getValue(attr));
                                System.out.println("INSTANCE ORIGINAL ATTR VAL: " + i_o.getValue(attr));

                                 */

                            }
                            if (instance.content.equals("I5") && z == 1) {
                                //System.out.println("I5 ATTR NOT CHANGED: " + attr);
                                //System.out.println("I5 VALUE: " + ((InstanceKnowledge) (instance.getValue(attr))).content);
                            }
                        }
                    }
                }
                e_init = snapshotEnv(e);
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
            e_init = snapshotEnv(e_master);
            e = snapshotEnv(e_master);
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
        e = snapshotEnv(e_master);
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
        System.out.println("SVS: ");
        for (Object[] o : specificVectors) {
            for (Object i: o) {
                System.out.print(((Knowledge) i).content + ", ");
            }
            System.out.println();
        }
        /*
        init q table
        format for each entry: {blueQ, redQ}
         */
        for (Object[] ignored : specificVectors) {
            qTable.add(new double[] {0.0, 0.0});
        }

        /*
        episodic training loop
         */
        int num_of_term_episodes = 0;
        for (int i = 0; i < EPISODE_THRESHOLD; i++) {
            epsilon = EPSILON_MIN + ((EPSILON_MAX - EPSILON_MIN) * Math.exp(-EPSILON_DECAY * i));
            e = snapshotEnv(e_master);
            int z = 0;
            int actionID = 0;
            while (z < 99 && !goalSatisfied()) {
                /*
                epsilon-greedy
                 */
                Random random = new Random();
                Object[] currState = getCurrentSpecVector(e_delta, attr_space, specificVectors.getFirst().length);
                if (random.nextDouble() < epsilon) {
                    //System.out.println("EXPLORE, " + epsilon);
                    // exploration
                    if (random.nextInt(2) == 0) {
                        blue_transition();
                    } else {
                        red_transition();
                    }

                    String cp = ((InstanceKnowledge) (i6.getValue("C1 pot"))).content;
                    if (Objects.equals(cp, i5.content)) {
                        //System.out.println("CP1->C1: " + cp);
                    }
                } else {
                    //System.out.println("EXPLOIT, " + epsilon);
                    // exploitation (use argmax)
                    /*
                    Construct current specific vector by finding current values of delta attrs
                    Specific vectors are equivalent to states in practical QL, so this current spec vector is the current state
                     */
                    // conduct argmax using current state
                    actionID = q_argmax(currState, qTable, specificVectors);
                    // TODO: Implement all action execution handling in general in ActionController
                    /*
                    this section is analogous to the transition function
                     */
                    if (actionID == 0) {
                        blue_transition();
                    } else if (actionID == -1) {
                        //System.out.println("Could not find the current state in specific vector space");
                        return;
                    } else {
                        red_transition();
                    }
                }

                Object[] newState = getCurrentSpecVector(e_delta, attr_space, specificVectors.getFirst().length);
                double reward = rewardFuncV1();
                String cp = ((InstanceKnowledge) (i6.getValue("C1 pot"))).content;
                String col = ((InstanceKnowledge) (i5.getValue("C2 color"))).content;
                /*
                if (Objects.equals(cp, i5.content)) {
                    System.out.println("R:" + reward);
                }

                 */
                if (Objects.equals(col, i1.content)) {
                    //System.out.println("P3->C2: " + col);
                }
                System.out.println("Searching for: " + Arrays.toString(convertToIDVector(currState)));
                int svIndex = svsIndexOf(specificVectors, currState);

                //System.out.println("CURR STATE INDEX: " + svIndex);
                    /*
                    for (Object o : currState) {
                        System.out.print(((Knowledge) o).content + ", ");
                    }

                     */
                System.out.println("SV INDEX: " + svIndex);
                qTable.get(svIndex)[actionID] =
                        qTable.get(svIndex)[actionID] +
                                (ALPHA * (reward + (GAMMA * q_max(newState, qTable, specificVectors)) - // TODO: ***ISSUE HERE***
                                        qTable.get(svIndex)[actionID]));
                z += 1;
            }
            if (z == 0 && goalSatisfied()) {
                System.out.println("The episode somehow ended early --> means the env is already solved somehow?");
                num_of_term_episodes += 1;
            }
        }

        System.out.println("-------------------- Q TABLE -----------------------");
        System.out.println("NUM OF TERM EPISODES: " + num_of_term_episodes);
        for (double[] d : qTable) {
            for (double i: d) {
                System.out.print(i + ", ");
            }
            System.out.println();
        }

        // TODO: Create a better way to visualize the Q table, especially the different combinations of specific vectors and their specific values

        /*
        EVALUATION
         */
        e = snapshotEnv(e_master);
        int iters = 0;
        /*
        while (!goalSatisfied()) {
            e = e_master;
            Object[] currState = getCurrentSpecVector(e_delta, attr_space, specificVectors.getFirst().length);

            // conduct argmax using current state
            int actionID = q_argmax(currState, qTable, specificVectors);
            // TODO: Implement all action execution handling in general in ActionController
                    //this section is analogous to the transition function
            if (actionID == 0) {
                blue_transition();
            } else if (actionID == -1) {
                System.out.println("Could not find the current state in specific vector space");
                System.out.println("Q TABLE: ");
                for (double[] d : qTable) {
                    System.out.println(Arrays.toString(d));
                }
                return;
            } else {
                red_transition();
            }
            iters += 1;
        }
        System.out.println("FINAL EVAL TOOK: " + iters);

         */
    }

}
