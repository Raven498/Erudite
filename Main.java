import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        KnowledgeBase tkb = new KnowledgeBase();

        //TEST TKB ENV_SET
        ArrayList<Environment> test_env_set = new ArrayList<>();
        Environment env = new Environment();
        Knowledge k = new Knowledge("TEST ALGO", "TEST", Agent.KClasses.ALGORITHM);
        env.addKnowledge(k);
        test_env_set.add(env);
        tkb.setEnvSet(test_env_set);

        //REAL TKB ENV_SET (EXTRACTION FROM SQLITE DB)

        Agent agent = new Agent(tkb);
        agent.cycle();
    }
}
