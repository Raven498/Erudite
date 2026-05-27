package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.*;

public class EpsilonManager {
    private static double training_prob = 1.0;
    private static final int LOTTERY_SAMPLE_SIZE = 10;

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

    private static void lottery() {
        Random random = new Random();
        int min_index = random.nextInt(AccumulationManager.getAKBSize());
        int max_index = random.nextInt(AccumulationManager.getAKBSize());
        /*
        Cases:
        1. min_index < max_index: scale to LOTTERY_SAMPLE_SIZE (such that (max_index - min_index) + 1 = LOTTERY_SAMPLE_SIZE)
        2. min_index = max_index: scale to LOTTERY_SAMPLE_SIZE (such that (max_index - min_index) + 1 = LOTTERY_SAMPLE_SIZE)
        3. min_index > max_index: set max_index = min_index, scale to LOTTERY_SAMPLE_SIZE (such that (max_index - min_index) + 1 = LOTTERY_SAMPLE_SIZE)
        Scaling to LOTTERY_SAMPLE_SIZE:

         */
        max_index += (Math.abs(min_index - max_index) + LOTTERY_SAMPLE_SIZE);


    }

    private static void welfare() {

    }

    private static void pes() {

    }

    private static void pas() {

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
