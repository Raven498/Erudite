import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Random;
public class Agent {
    public enum KClasses{
        RELATIONSHIP,
        POLICY
    }
    KnowledgeBase trueKB;

    public Agent(KnowledgeBase trueKB){
        this.trueKB = trueKB;
    }

    //Mainloop method
    public void cycle(){
        for(Environment env : trueKB.getEnvSet()){

        }
    }

    public void accumulate(){
        for(Environment env : trueKB.getEnvSet()){
            /*
            for(Environment approx_env : approxKB.getEnvSet()){
                if(approx_env.getEnvId() == env.getEnvId()){
                    continue;
                }
            }
             */
            approx(env);
        }
    }

    public void approx(Environment true_env){
        for(Knowledge k : true_env.getKnowledge()){
            if(k.kclass == KClasses.POLICY){

            }
            if(k.kclass == KClasses.RELATIONSHIP){

            }
        }
    }

    public double reward(State s){
        return 1.8 * s.getTarget();
    }

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

    public void PRL(){
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

        for(double target : t){
            State state = new State(target);
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


        //GOAL CREATION


        //STEP 2: PRL-POWERED EXPLORATION



    }
}
