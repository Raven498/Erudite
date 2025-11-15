package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Concept;
import com.erudite.erudite_node.model.Environment;
import com.erudite.erudite_node.model.Instance;
import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.List;
import java.util.Random;

import static com.erudite.erudite_node.management.Director.getTrueConcept;

/*
Contains custom unit tests to verify all management functionality
 */
public class InterfaceSimulator {
    /*
    STANDARD REQ DISTRIB ORDER:
    RELATIONSHIP, POLICY, ALGORITHM, CONCEPT, INSTANCE, STATE
     */
    public static double[] k_type_req_distrib_1 = new double[] {0.0, 0.0, 0.0, 0.5, 0.5, 0.0};
    public static double[] k_type_req_distrib_2 = new double[] {};
    public static double[] k_type_req_distrib_3 = new double[] {};
    public static double[] current_req_distrib = k_type_req_distrib_1;
    public static double[] current_distrib;

    public static Knowledge generateKnowledge(Agent.KClasses kType){
        switch(kType){
            case Agent.KClasses.INSTANCE:
                return new Instance();
            case Agent.KClasses.CONCEPT:
                return new Concept();
            default:
                return null;
        }
    }

    // Test for expansion - construct new true env, pass to Director, verify distribution levels
    public static Environment getEnv(int akbSize){
        Environment environment = new Environment();
        if(akbSize != 0){
            Random random = new Random();
            // For each k type, generate random number of knowledge (below 10)
            for(Agent.KClasses k : Agent.KClasses.values()){
                for(int i = 0; i < random.nextInt(); i++){
                    environment.addKnowledge(generateKnowledge(k));
                }
            }
        } else{
            updateDistrib(0.0);
        }
    }

    public static void updateDistrib(double value){
        for(int i = 0; i < current_distrib.length; i++){
            current_distrib[i] = value;
        }
    }


    // Focus --> explore
    public static List<Knowledge> getDelta(){
        System.out.println("Invalid Expansion"); // TODO: Replace with custom exception
    }
/*
    // Focus --> interact
    public static List<Knowledge> getDelta(){

    }\

 */
}
