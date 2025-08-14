package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.Knowledge;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EpsilonManager {
    private static double training_prob = 1.0;

    public static void epsilon(UUID true_env_id, List<Knowledge> knowledge) {
        double s = Math.random();
        if (s <= training_prob) {
            AccumulationManager.accumulate(true_env_id, knowledge);
        } else {
            InteractionManager.interact();
        }
    }
}
