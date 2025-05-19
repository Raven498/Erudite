import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args){
        KnowledgeBase tkb = new KnowledgeBase();

        //TEST TKB ENV_SET
        ArrayList<Environment> test_env_set = new ArrayList<>();
        Environment env = new Environment();
        Concept k = new Concept("POLYNOMIAL", "int a, int x, int n", Agent.KClasses.CONCEPT, new ArrayList<>(
                Arrays.asList("a", "x", "n") //(NOTE: attr labels should be autopopulated by parsing content field)
        ));
        env.addKnowledge(k);
        test_env_set.add(env);
        tkb.setEnvSet(test_env_set);

        for(int i = 0; i < 20; i++){
            Random r = new Random();
            GenState g = new GenState(
                    new Instance(k, new ArrayList<>(Arrays.asList(r.nextInt(10), "x", r.nextInt(10)))));
        }

        //REAL TKB ENV_SET (EXTRACTION FROM SQLITE DB)

        Agent agent = new Agent(tkb);
        agent.cycle();
    }
}
