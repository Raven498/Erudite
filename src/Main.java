import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args){
        KnowledgeBase tkb = new KnowledgeBase();

        //TEST TKB ENV_SET
        ArrayList<Environment> test_env_set = new ArrayList<>();
        Environment env = new Environment();
        Concept k = new Concept("MONOMIAL", "int a, int x, int n", Agent.KClasses.CONCEPT, new ArrayList<>(
                Arrays.asList("a", "x", "n") //(NOTE: attr labels should be autopopulated by parsing content field)
        ));

        ArrayList<Object> out = new ArrayList<>(Arrays.asList(
                "a",
                "x",
                "n"
        ));
        ArrayList<String> acts = new ArrayList<>(Arrays.asList(
                "MULT",
                "ID",
                "ADD"
        ));

        ArrayList<Object> param = new ArrayList<>(Arrays.asList(
                Arrays.asList(
                        "I.a",
                        "I.n"
                ),
                "I.x",
                Arrays.asList(
                        "I.n",
                        Arrays.asList(
                                "NEG",
                                1
                        )
                )
        ));

        ArrayList<GenState> ts = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            Random r = new Random();
            Knowledge g = new GenState("STATE " + i, "I LOVE VALLIUM", Agent.KClasses.STATE, "ALGO-POWER",
                    new Instance(k, new ArrayList<>(Arrays.asList(r.nextInt(10), "x", r.nextInt(10)))));
            env.addKnowledge(g);
        }
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
