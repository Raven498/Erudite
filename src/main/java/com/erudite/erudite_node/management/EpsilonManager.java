package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Environment;
import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.service.Agent;

import java.util.*;

public class EpsilonManager {
    private static double accum_prob = 1.0;
    private static final int LOTTERY_SAMPLE_SIZE = 10;
    private static final double WELFARE_SUBSET_THRESHOLD = 0.10;

    public static void expansion(UUID true_env_id) {
        AccumulationManager.expand(true_env_id);
    }

    public static void epsilon(UUID env_id) {
        double s = Math.random();
        if (s <= accum_prob) { // Train
            if(AccumulationManager.verify()){
                AccumulationManager.accumulate(env_id, getRequiredKnowledge(AccumulationManager.getRequiredKTypes()));
            }
        } else { // Interact
            InteractionManager.interact();
        }
    }

    /*
    Constant-sized implementation of lottery system
    (AKB env samples will always be size LOTTERY_SAMPLE_SIZE)
     */
    public static void lotteryConstant() {
        // Sample AKB env set for random constant-sized subset of envs
        Random random = new Random();
        var env_set = AccumulationManager.getAKB().getEnvSet();
        Map<UUID, Environment> sample = new HashMap<>();
        while (sample.size() < LOTTERY_SAMPLE_SIZE) {
            int index = random.nextInt(env_set.size());
            Environment env = (Environment) env_set.values().toArray()[index];
            UUID uuid = (UUID) env_set.keySet().toArray()[index];
            if (!sample.containsKey(uuid)) {
                sample.put(uuid, env);
            }
        }

        // Conduct epsilon process (focus cycle) sequentially for each env in sample
        for (Object uuid : sample.keySet().toArray()) {
            epsilon((UUID) uuid);
        }
    }

    /*
    Random-sized implementation of lottery system
    (AKB env samples will have a random size between 1 and the AKB's env size)
     */
    public static void lotteryRand() {
        // Sample AKB env set for random constant-sized subset of envs
        Random random = new Random();
        var env_set = AccumulationManager.getAKB().getEnvSet();
        Map<UUID, Environment> sample = new HashMap<>();
        int lottery_sample_size = random.nextInt((env_set.size() - 1) + 1) + 1;
        while (sample.size() < lottery_sample_size) {
            int index = random.nextInt(env_set.size());
            Environment env = (Environment) env_set.values().toArray()[index];
            UUID uuid = (UUID) env_set.keySet().toArray()[index];
            if (!sample.containsKey(uuid)) {
                sample.put(uuid, env);
            }
        }

        // Conduct epsilon process (focus cycle) sequentially for each env in sample
        for (Object uuid : sample.keySet().toArray()) {
            epsilon((UUID) uuid);
        }
    }

    public static void welfare() {
        // Pick env subset with least k-size
        // (subset is (WELFARE_SUBSET_THRESHOLD)% of AKB env set)
        var env_set = AccumulationManager.getAKB().getEnvSet();
        Map<UUID, Environment> subset = new HashMap<>();
        int subset_size = (int) (env_set.size() * WELFARE_SUBSET_THRESHOLD);
        int min_env_size = 0;
        for (int i = 0; i < subset_size; i++) {
            for (int j = 0; j < env_set.size(); j++){
                Environment env = (Environment) env_set.values().toArray()[j];
                UUID uuid = (UUID) env_set.keySet().toArray()[j];
                if ((!subset.containsKey(uuid)) && (env.getKnowledge().size() <= min_env_size)) {
                    subset.put(uuid, env);
                    min_env_size = env.getKnowledge().size();
                }
            }
        }

        // Conduct epsilon process (focus cycle) sequentially for each env in the subset
        for (Object uuid : subset.keySet().toArray()) {
            epsilon((UUID) uuid);
        }
    }

    public static double getEpsilonProb(){
        return accum_prob;
    }

    public static void releaseAccumulationSpace(double scale){
        accum_prob -= scale;
    }

    public static void addAccumulationSpace(double scale){
        accum_prob += scale;
    }

    private static ArrayList<Knowledge> getRequiredKnowledge(ArrayList<Agent.KClasses> requiredKTypes){
        ArrayList<Knowledge> knowledge = new ArrayList<>();
        for(Agent.KClasses kType : requiredKTypes){
            knowledge.add(Director.request(kType)); // TODO: Handle null values from request
        }
        return knowledge;
    }
}
