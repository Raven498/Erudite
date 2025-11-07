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
    public static final double[] K_TYPE_REL_DISTRIBUTION_REQ = new double[6];

    public static Environment initApproxEnv() {
        return new Environment();
    }

    public static void accumulate(UUID true_env_id, List<Knowledge> knowledge) {
        List<Knowledge> approxes = new ArrayList<>();
        for (Knowledge k : knowledge) {
            approxes.add(agent.approx(k));
        }
        var approx_env = new Environment();
        approx_env.addKnowledge(approxes);
        akb.addEnv(true_env_id, approx_env);
        System.out.println("AKB: " + akb);
    }
}