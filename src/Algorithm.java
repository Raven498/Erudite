import java.util.ArrayList;

public class Algorithm extends Knowledge {
    public Concept input;
    public Concept output;
    public ArrayList<String> outputs;
    public ArrayList<String> actions;
    public ArrayList<Object> parameters;

    public TrainEnv train_env;

    public Algorithm(String name, String content, Agent.KClasses kclass, Concept input, Concept output, ArrayList<String> outputs, ArrayList<String> actions, ArrayList<Object> parameters, TrainEnv train_env) {
        super(name, content, kclass);
        this.input = input;
        this.output = output;
        this.outputs = outputs;
        this.actions = actions;
        this.parameters = parameters;
        this.train_env = train_env;
    }

    @Override
    public Algorithm approx(){
        Algorithm k_a = this;
        //System.out.println("PENIS");
        //PARTIAL APPROX



        return k_a;
    }
}
