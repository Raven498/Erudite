package com.erudite.erudite_node.management;

import com.erudite.erudite_node.model.*;
import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The Director is responsible for determining the "needs" of this node based on currently accumulated knowledge,
 * used & available memory/resources, etc, and orchestrating the appropriate actions as such. Primarily, it determines the
 * pacing and order of training & interaction, as well as throttling training and interaction commands through an algorithm
 * similar to the epsilon-greedy policy. It is also the only class that can send outgoing endpoint requests to external
 * entities such as the Interface and other nodes.
 * TODO: Revise this shitty explanation of what the Director does
 */
public class Director {

    private static void initEnv(){

    }

/*
FORMAL ARCHITECTURE FOR DIRECTOR:
EPSILON MANAGER: Orchestrates and regulates pacing/order of accumulation vs interaction FOR ALL ENVIRONMENTS of a TKB
    Description: Orchestrates accumulation or interaction processes nondeterministically, monitors size of AKB & used memory/resources to manage processes' relative execution probabilities and orchestrates
    other appropriate knowledge/memory management actions
ACCUMULATION MANAGER: Orchestrates and regulates accumulation processes to meet AKB requirements it defines, tracks information about the AKB (manages it for all environments)
    Description: Creates and kicks off an accumulation process and acts as an interface for all AKB information. In director context, tracks and balances the relative approx percentage distribution across all
    k types and communicates with Epsilon Manager to find respective Epsilon process managing its accumulation process and get sufficient accumulation space for balancing distributions & meeting any other defined
    AKB requirements
INTERACTION MANAGER: Orchestrates and regulates interaction processes to meet interaction requirements it defines, tracks information about all environment interactions
    Description: Creates and kicks off an interaction process and acts as an interface for all interaction information. Communicates with Epsilon Manager to find respective Epsilon process managing its interaction
    process and get sufficient accumulation space for meeting any defined interaction requirements
*/
    public static void direct(){

    }

    public static void test_direct(){
        KnowledgeBase tkb = new KnowledgeBase();

        //TEST TKB ENV_SET
        ArrayList<Environment> test_env_set = new ArrayList<>();
        Environment env = new Environment();
        Concept k = new Concept("MONOMIAL", "int a, int x, int n", Agent.KClasses.CONCEPT, new ArrayList<>(
                Arrays.asList("a", "x", "n") //(NOTE: attr labels should be autopopulated by parsing content field)
        ));

        ArrayList<String> out = new ArrayList<>(Arrays.asList(
                "a",
                "x",
                "n"
        ));

        ArrayList<String> acts = new ArrayList<>(Arrays.asList(
                "MULT",
                "ID",
                "ADD"
        ));

        //Figure out way to clean up this syntax for readability
        ArrayList<AlgoParameters> param = new ArrayList<>(Arrays.asList(
                new AlgoParameters(Arrays.asList(
                        new AlgoParameters("a"),
                        new AlgoParameters("n")
                )),
                new AlgoParameters("x"),
                new AlgoParameters(
                        Arrays.asList(
                                new AlgoParameters("n"),
                                new AlgoParameters(Arrays.asList(
                                        new AlgoParameters("NEG"),
                                        new AlgoParameters("1")
                                ))
                        )
                )
        ));

        ArrayList<GenState> ts = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            Random r = new Random();
            GenState g = new GenState( "ALGO-POWER",
                    new Instance(k));
            g.f.addValue("a", Integer.toString(r.nextInt(10)));
            g.f.addValue("x", "x");
            g.f.addValue("n", Integer.toString(r.nextInt(10)));
            ts.add(g);
        }
        System.out.println("TS: " + ts);
        RewardHandler rh = new RewardHandler(50, 25, k, "a", "3", "a", "a");
        Algorithm a = new Algorithm("POWER", "O.a = I.a * I.n, O.x = I.x, O.n = I.n - 1", Agent.KClasses.ALGORITHM, k, k, out, acts, param, ts, rh);

        env.addKnowledge(k);
        env.addKnowledge(a);

        test_env_set.add(env);
        tkb.setEnvSet(test_env_set);

        //REAL TKB ENV_SET (EXTRACTION FROM SQLITE DB)

        Agent agent = new Agent(tkb);
        agent.cycle();
    }

}
