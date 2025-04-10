import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Random;
public class Agent {
    public enum KClasses{
        RELATIONSHIP,
        POLICY,
        ALGORITHM
    }
    KnowledgeBase trueKB;
    KnowledgeBase approxKB;

    public Agent(KnowledgeBase trueKB){
        this.trueKB = trueKB;
    }

    /*
    TKB input content parsing algorithm
    May be different depending on environment type
     */
    public Algorithm parseAlgo(String name, String content){
        Algorithm algo = new Algorithm(name, content, KClasses.ALGORITHM);
        algo.name = name;

        //STAGE 1: INPUT PARSING (INPUT --> PARSED)
        String[] c_reg = content.split("///");
        for(String c : c_reg){

        }

        return algo;
    }

    //Mainloop method
    public void cycle(){
        accumulate();
        //(PRL SIM NEXT BEFORE INTERACTION)
        /*
        FEED PREPARED STATE-REWARD TRAJECTORY DATA TO PRL ALGORITHM
        (INTERACTION WILL INVOLVE SYSTEMATIC ITERATION OF STATES TO CREATE TRAJ)
         */
        interact();
    }

    public void accumulate(){
        KnowledgeBase akb = new KnowledgeBase();
        ArrayList<Environment> approx_env_set = new ArrayList<>();
        for(Environment env : trueKB.getEnvSet()){
            approx_env_set.add(approx(env));
        }
        akb.setEnvSet(approx_env_set);
        approxKB = akb;
    }

    //Interaction Phase
    public void interact(){

    }

    /*
    Reads all environmental data + constructs environmental approximation
     */
    public Environment approx(Environment true_env){
        Environment approx_env = new Environment();
        for(Knowledge k : true_env.getKnowledge()){
            if(k.kclass == KClasses.POLICY){

            }
            if(k.kclass == KClasses.RELATIONSHIP){

            }
            if(k.kclass == KClasses.ALGORITHM){
                Algorithm k_a = parseAlgo(k.name, k.content);
                approx_env.addKnowledge(k_a);
            }
        }
        return approx_env;
    }

    public double reward(State s){
        return 1.8 * s.getTarget();
    }

    public double func(double[] lsrl, double x){return (lsrl[0] * x) + lsrl[1];}

    public double[] regression(ArrayList<Double> x, ArrayList<Double> y) {
        double mean_x = 0;
        double mean_y = 0;
        for (int i = 0; i < x.size(); i++) { //Iteration through random states (state exploration)
            mean_x += x.get(i);
            mean_y += y.get(i);
        }
        mean_x /= x.size();
        mean_y /= x.size();

        double data_diff_sum = 0;
        double x_diff_sq_sum = 0;
        double m = 0;
        double b = 0;

        for(int i = 0; i < x.size(); i++){
            data_diff_sum += (x.get(i) - mean_x) * (y.get(i) - mean_y);
            x_diff_sq_sum += Math.pow((x.get(i) - mean_x), 2);
        }

        m = (data_diff_sum) / (x_diff_sq_sum);
        b = y.get(0) - (m * x.get(0));
        return new double[] {m, b};
    }

    public double stdev(ArrayList<Double> x){
        double mean_x = 0;
        for (int i = 0; i < x.size(); i++) {
            mean_x += x.get(i);
        }
        mean_x /= x.size();

        double stdev = 0;
        for(int i = 0; i < x.size(); i++){
            stdev += Math.pow((x.get(i) - mean_x), 2);
        }

        stdev /= x.size();
        stdev = Math.pow(stdev, 0.5);
        return stdev;
    }

    public State prl_learn(){
        boolean adjY = false;
        boolean xMode = false;
        ArrayList<Double> train_t = new ArrayList<>(); //training targets
        ArrayList<Double> f = new ArrayList<>(); //feature
        ArrayList<Double> r = new ArrayList<>(); //reward
        ArrayList<Double> t = new ArrayList<>(); //target

        ArrayList<ArrayList<Double>> x_reg = new ArrayList<>(); //contains f, t
        ArrayList<ArrayList<Double>> y_reg = new ArrayList<>(); //contains adj_y, r
        String[] filenames = {"data-normal.txt", "data-reward.txt"};
        for(String filename : filenames){
            ArrayList<Double> x = new ArrayList<>();
            ArrayList<Double> y = new ArrayList<>();
            try{
                File file = new File(filename);
                Scanner reader = new Scanner(file);
                while(reader.hasNextLine()){
                    String data = reader.nextLine();
                    while(!data.contains("ADJ Y:") && !data.contains("X:") && !data.isEmpty() && reader.hasNextLine()){
                        data = data.replace("\n", "");
                        if(adjY){
                            y.add(Double.parseDouble(data));
                        }
                        else if(xMode){
                            x.add(Double.parseDouble(data));
                        }

                        if(reader.hasNextLine()){
                            data = reader.nextLine();
                        }
                    }
                    if(data.contains("ADJ Y:")){
                        adjY = true;
                        xMode = false;
                    }
                    else if(data.contains("X:")){
                        xMode = true;
                        adjY = false;
                    }
                    else{
                        xMode = false;
                        adjY = false;
                    }
                }
                System.out.println(y);
                System.out.println(x);
                x_reg.add(x);
                y_reg.add(y);
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        f = x_reg.get(0);
        t = x_reg.get(1);
        train_t = y_reg.get(0);
        r = y_reg.get(1);

        ArrayList<State> states = new ArrayList<>();

        for(int i = 0; i < t.size(); i++){
            State state = new State(t.get(i), f.get(i));
            states.add(state);
        }

        //PRL ALGORITHM

        //STEP 1: PRELIM TRAJ
        int trajSize = 25;
        Random random = new Random();
        ArrayList<State> prelim_traj = new ArrayList<>();
        ArrayList<Double> prelim_targets = new ArrayList<>();
        ArrayList<Double> prelim_rewards = new ArrayList<>();
        for(int i = 0; i < trajSize; i++){ //Iteration through random states (state exploration)
            State s = states.get(random.nextInt(trajSize));
            prelim_traj.add(s);
            prelim_targets.add(s.getTarget());
            double reward = reward(s);
            prelim_rewards.add(reward);
        }

        double[] reward_lsrl = regression(prelim_targets, prelim_rewards);
        double[] target_lsrl = regression(f, train_t);
        System.out.println("REWARD LSRL: y =  " + reward_lsrl[0] + "x + " + reward_lsrl[1]);
        System.out.println("TARGET LSRL: y =  " + target_lsrl[0] + "x + " + target_lsrl[1]);
        System.out.println(0.80 * (stdev(train_t) / stdev(f)));

        double[] x_state_domain = new double[states.size()];
        for(int i = 0; i < states.size(); i++){
            x_state_domain[i] = states.get(i).getFeature();
        }

        //POLICY CREATION
        Goal r_goal;
        Goal t_goal;
        if(reward_lsrl[0] >= 0){
            r_goal = new Goal("max", reward_lsrl, target_lsrl);
        } else{
            r_goal = new Goal("min", reward_lsrl, target_lsrl);
        }

        if(target_lsrl[0] >= 0){
            t_goal = new Goal("max", target_lsrl, x_state_domain);
        } else{
            t_goal = new Goal("min", target_lsrl, x_state_domain);
        }

        //STEP 2: PRL-POWERED EXPLORATION
        double f_extr = 0;
        State final_s = null;
        int index = 0;
        if(t_goal.action.equals("max")){
            for(State s : states){
                if(s.getFeature() >= f_extr){
                    f_extr = s.getFeature();
                    final_s = s;
                    index = states.indexOf(final_s);
                }
            }
        }
        else{
            for(State s : states){
                if(s.getFeature() <= f_extr){
                    f_extr = s.getFeature();
                    final_s = s;
                }
            }
        }

        System.out.println(f_extr);
        System.out.println(index);
        return final_s;
    }

    /*
    POLICY METHODS NOTES:
    All methods represent generalized policies for any state and reward space - theoretical policies will only take
        state s as a parameter, with state space + learned rewards used internally in trained policies
    Policy generalizations (normal convergence, periodic divergence) take additional state parameters beyond generalized
    parameters of state/reward space - in practice, all states will be used for all state parameters during Q-value calculations
     */

    /*
        Optimal Convergence Policy:
        Converge to the state that provides maximum reward
    */
    public int optimal_convergence_policy(ArrayList<State> states, ArrayList<Double> rewards, State s){
        /*
        Find the state index producing max reward
         */
        double max = 0;
        for(double r : rewards){
            if(r > max){
                max = r;
            }
        }
        int max_index = rewards.indexOf(max);

        /*
        Return action depending on state index vs. optimal index
         */
        if(states.indexOf(s) < max_index){
            return 1;
        } else if(states.indexOf(s) > max_index){
            return 0;
        } else{
            return 2;
        }
    }

    /*
        Normal Convergence Policy:
        Converge to any given state
        (Generalization of Optimal Convergence Policy)
     */
    public int normal_convergence_policy(ArrayList<State> states, ArrayList<Double> rewards, State s, State c){
        /*
        Return action depending on state index vs. converge index
         */
        if(states.indexOf(s) < states.indexOf(c)){
            return 1;
        } else if(states.indexOf(s) > states.indexOf(c)){
            return 0;
        } else{
            return 2;
        }
    }

    /*
        Cyclical Divergence Policy:
        Go to closest "end" of state space from starting state (s_1 or s_n), then oscillate infinitely between "end" state
        and the state before.
    */
    public int cyclical_divergence_policy(ArrayList<State> states, State s){
        int c = states.indexOf(s);
        if(c < (states.size() - 1) - c){
            return 0;
        } else if(c > (states.size() - 1) - c){
            return 1;
        } else{
            Random rand = new Random();
            return rand.nextInt(2);
        }
    }

    /*
       Periodic Divergence Policy:
       Go to closest "bound state" from starting state (s_L1 or s_L2), then oscillate infinitely between bound state and
       the state before.
    */
    public int periodic_divergence_policy(ArrayList<State> states, State s, State L, State U){
        int c = states.indexOf(s);
        int L1 = states.indexOf(L);
        int L2 = states.indexOf(U);
        if(Math.abs(L1 - c) < L2 - c){
            return 0;
        } else if(Math.abs(L1 - c) > L2 - c){
            return 1;
        } else{
            Random rand = new Random();
            return rand.nextInt(2);
        }
    }

    /*
    PRL Demonstration for Discrete State Spaces (uses the reward function learned in prl_learn)
    This method utilizes a rudimentary model to simulate model interactions, such as environmental
    transitions, collecting reward, etc.
        - Ground-truth model: MDP w/ ternary action space (left, right, stay), discrete state space (set of state vectors),
        environmental transition function (left = previous state, right = next state), and a positive + linear reward function
        - Current practical model used in this demonstration: ArrayList<State> (for state space), list index increment/decrement,
        (for action space + env transition func), double[] (for learned reward function from prl_learn)

    Action Space One-Hot Encoding:
    0: Left
    1: Right
    2: Stay
     */
    //Assume all states are initialized with targets and features
    public void prl_interact(ArrayList<State> states, double[] learned_reward){
        ArrayList<Double> rewards = new ArrayList<>();
        double max = 0;
        for(State s : states){
            double r = (learned_reward[0] * s.getTarget()) + learned_reward[1];
            rewards.add(r);
            if(r > max){
                max = r;
            }
        }

        /*
        Generalized Demonstration:
        Test Optimal Convergence Policy against:
            - Normal Convergence Policy
            - Cyclical Divergence Policy
            - Periodic Divergence Policy
        by measuring return across all states
        Goal: Prove that only the Optimal Convergence Policy maximizes return
        */
        
        

    }


}
