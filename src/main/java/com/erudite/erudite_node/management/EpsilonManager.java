package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EpsilonManager {
    private static double training_prob = 1.0;

    public static void epsilon(UUID true_env_id) {
        double s = Math.random();
        if (s <= training_prob) { // Train
            if(AccumulationManager.verify()){
                AccumulationManager.accumulate(true_env_id, getRequiredKnowledge(AccumulationManager.getRequiredKTypes()));
            }
        } else { // Interact
            InteractionManager.interact();
        }
    }

    public static double getEpsilonProb(){
        return training_prob;
    }

    public static void releaseAccumulationSpace(double scale){
        training_prob -= scale;
    }

    public static void addAccumulationSpace(double scale){
        training_prob += scale;
    }

    private static ArrayList<Knowledge> getRequiredKnowledge(ArrayList<Agent.KClasses> requiredKTypes){
        ArrayList<Knowledge> knowledge = new ArrayList<>();
        for(Agent.KClasses kType : requiredKTypes){
            knowledge.add(Director.request(kType)); // TODO: Handle null values from request
        }
        return knowledge;
    }
}
