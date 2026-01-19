package com.erudite.erudite_node.management;

import com.erudite.erudite_node.EruditeApplication;
import com.erudite.erudite_node.model.*;
import com.erudite.erudite_node.service.Agent;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.type.*;
import okhttp3.*;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The Director is responsible for determining the "needs" of this node based on currently accumulated knowledge,
 * used & available memory/resources, etc, and orchestrating the appropriate actions as such. Primarily, it determines the
 * pacing and order of training & interaction, as well as throttling training and interaction commands through an algorithm
 * similar to the epsilon-greedy policy. It is also the only class that can send outgoing endpoint requests to external
 * entities such as the Interface and other nodes.
 * TODO: Revise explanation of what the Director does
 */
public class Director {
    private static double expansion_prob = 1.0;
    public static KnowledgeBase tkb = new KnowledgeBase();

    public static Environment initEnv() {
        Environment env = new Environment();
        env.addKnowledge(getTrueKnowledge());
        return env;
    }

    public static Knowledge request(Agent.KClasses kType){
        switch(kType){
            case Agent.KClasses.INSTANCE:
                return getTrueInstance();
            case Agent.KClasses.CONCEPT:
                return getTrueConcept();
            default:
                return null;
        }
    }

    public static List<Knowledge> getTrueKnowledge() {
        ArrayList<Knowledge> trueKnowledge = new ArrayList<>();
        Concept concept = getTrueConcept();
        if (concept != null) {
            trueKnowledge.add(concept);
        }
        Instance instance = getTrueInstance();
        if(instance != null){
            trueKnowledge.add(instance);
        }
        return trueKnowledge;
    }

    public static InstanceTest getTrueInstanceTest() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).build();

        try {
            Request request = new Request.Builder()
                    .url("http://localhost:8080/instance")
                    .addHeader("Content-Type", "application/json")
                    .get()
                    .build();

            // Execute request and get response as JSON string
            ResponseBody response = client.newCall(request).execute().body();
            String responseJson = response.string();

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(responseJson, InstanceTest.class);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Instance getTrueInstance()  {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).build();

        Request request = new Request.Builder()
                .url("http://localhost:8080/instance")
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        // Execute request and get response as JSON string
        try{
            ResponseBody response = client.newCall(request).execute().body();
            String responseJson = response.string();
            System.out.println("RESPONSE JSON (INSTANCE): " + responseJson);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(responseJson);

            if(responseNode.get("status") != null && Objects.equals(responseNode.get("status").asText(), "418")){
                // Indicate to mainloop (from EruditeApplication) that it should wait one minute from current time
                EruditeApplication.delay = true;
                return null;
            } else{
                EruditeApplication.delay = false;
            }

            InstanceTest instanceTest = mapper.readValue(responseJson, InstanceTest.class);


            Instance instance = new Instance();
            instance.convertFromTest(instanceTest);
            return instance;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Concept getTrueConcept(){
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).build();

        Request request = new Request.Builder()
                .url("http://localhost:8080/concept")
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        // Execute request and get response as JSON string
        try{
            ResponseBody response = client.newCall(request).execute().body();
            String responseJson = response.string();
            System.out.println("RESPONSE JSON (CONCEPT): " + responseJson);
            ObjectMapper mapper = new ObjectMapper();

            JsonNode responseNode = mapper.readTree(responseJson);

            // Handling Gemini RPM/TPM rate limits
            if(responseNode.get("status") != null && Objects.equals(responseNode.get("status").asText(), "418")){
                // Indicate to mainloop (from EruditeApplication) that it should wait one minute from current time
                EruditeApplication.delay = true;
                return null;
            } else{
                EruditeApplication.delay = false;
            }

            // TODO: Handle Gemini RPD rate limits

            ObjectReader reader = mapper.readerFor(new TypeReference<ArrayList<String>>() {});
            ArrayList<String> attrs = reader.readValue(responseNode.get("attrs")); // lookup correct JSON attr label
            ArrayList<String> behaviors = reader.readValue(responseNode.get("behaviors")); // lookup correct JSON attr label
            Concept concept = new Concept(responseNode.get("name").asText(), "", Agent.KClasses.CONCEPT,  attrs, behaviors); // lookup correct JSON attr label
            return concept;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void direct() {
        // Master epsilon
        double s = Math.random();
        if (s <= expansion_prob) { // Expansion
            // Init true env, add to TKB
            var env = new Environment(getTrueKnowledge());
            UUID true_env_id = UUID.randomUUID();
            tkb.addEnv(true_env_id, env);
            EpsilonManager.epsilon(true_env_id);
        } else { // Focus

        }
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


    Epsilon-greedy policy for training vs interaction:
1. e = training probability
2. e_i = 100%
3. Use AKB size to throttle training prob (e = -mx, where x = AKB size)
May need to do this for each env according to each env's AKB size
4. Converse of e = interaction probability

(This occurs per environment)
Sample to decide training vs interaction
If training: hit env interface endpoint for instance, algo, etc.
How to orchestrate diff knowledge type approximations (during training)? // (what order should they be approxed in)?
One option: approx according to rel approx percentage of each k type in AKB
Rel approx % = # of k type approximations in AKB / # of total approximations in AKB (AKB size)
"Balance" distribution of k type approximations so it reaches standard spec
While training, need an algorithm that automatically adjusts the k type to be approxed to keep k type approx distribution balanced
Will pick random k type initially & when distributions are balanced adequately
Because this algorithm would work within the epsilon-greedy policy for training vs interaction, the algorithm may not consistently enforce specific reqs for each k type.
To handle this, rel approx requirements must be estimated ranges for each k type - must check during epsilon-greedy if ranges have been exceeded/unfulfilled and if so, increase training prob appropriately to provide more opportunities to handle it
Interactions MAY create new approximations - need to develop example cases where this could happen, but supporting this will involve either allowing interactive processes to increase training prob, or allowing them to directly create approx, letting epsilon-greedy manage any changes necessary to maintain distribution

*/

    public static void test_direct(){
        KnowledgeBase tkb = new KnowledgeBase();

        //TEST TKB ENV_SET
        ArrayList<Environment> test_env_set = new ArrayList<>();
        Environment env = new Environment();
        Concept k = new Concept("MONOMIAL", "int a, int x, int n", Agent.KClasses.CONCEPT,
                new ArrayList<>(Arrays.asList("a", "x", "n")), new ArrayList<>()); //(NOTE: attr labels should be autopopulated by parsing content field)));

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
                    new OldInstance(k));
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

        Agent agent = new Agent();
        agent.cycle();
    }

}
