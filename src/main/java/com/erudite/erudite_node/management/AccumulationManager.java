package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Environment;
import com.erudite.erudite_node.model.Knowledge;
import com.erudite.erudite_node.model.KnowledgeBase;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccumulationManager {
    public static KnowledgeBase akb = new KnowledgeBase();
    public static Agent agent = new Agent();
    private static final double ACCUM_THROTTLE = 0.05;
    public static final double[] K_TYPE_REL_DISTRIBUTION_REQ = new double[Agent.kTypes];

    public static void expand(UUID true_env_id) {
        var approx_env = new Environment();
        akb.addEnv(true_env_id, approx_env);
    }

    public static void accumulate(UUID env_id, List<Knowledge> knowledge) {
        List<Knowledge> approxes = new ArrayList<>();
        for (Knowledge k : knowledge) {
            approxes.add(agent.approx(k));
        }
        var approx_env = akb.getEnv(env_id);
        approx_env.addKnowledge(approxes);
        updateDistrib();
        System.out.println("AKB: " + akb);
    }

    /*
    TODO: Document
     */
    public static ArrayList<Agent.KClasses> getRequiredKTypes(){
        double[] currentDistribution = akb.getCurrentTypeDistribution();
        ArrayList<Agent.KClasses> kTypes = new ArrayList<>();
        for(int i = 0; i < currentDistribution.length; i++){
            if(currentDistribution[i] < K_TYPE_REL_DISTRIBUTION_REQ[i]){
                Agent.KClasses kType = Agent.KClasses.values()[i];
                kTypes.add(kType);
            }
        }
        return kTypes;
    }

    public static boolean verify(){
        ArrayList<Agent.KClasses> kTypes = getRequiredKTypes();
        if(kTypes.isEmpty()){
            EpsilonManager.releaseAccumulationSpace(ACCUM_THROTTLE);
            return false;
        } else{
            EpsilonManager.addAccumulationSpace(ACCUM_THROTTLE);
            return true;
        }
    }

    public static KnowledgeBase getAKB(){
        return akb;
    }

    private static void updateDistrib(){
        double[] current = akb.getCurrentTypeDistribution();
        for(int i = 0; i < current.length; i++){
            current[i] = (double) getKTypeSize(Agent.KClasses.values()[i]) / getAKBSize();
        }
        akb.setCurrentTypeDistribution(current);
    }

    public static int getKTypeSize(Agent.KClasses kType){
        int kTypeSize = 0;
        for(Environment env : akb.getEnvSet().values()){
            for(Knowledge k : env.getKnowledge()){
                if(k.getKClass() == kType){
                    kTypeSize += 1;
                }
            }
        }
        return kTypeSize;
    }

    public static int getAKBSize(){
        int akbSize = 0;
        for(Environment env : akb.getEnvSet().values()){
            akbSize += env.getKnowledge().size();
        }
        return akbSize;
    }
}