import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm extends Knowledge {
    public ArrayList<GenState> states;
    public Concept input;
    public Concept output;
    public ArrayList<Object> outputs;
    public ArrayList<String> actions;
    public ArrayList<Object> parameters;
    public RewardHandler rh;

    public Algorithm(String name, String content, Agent.KClasses kclass, Concept input, Concept output, ArrayList<Object> outputs, ArrayList<String> actions, ArrayList<Object> parameters, ArrayList<GenState> states, RewardHandler rh) {
        super(name, content, kclass);
        this.input = input;
        this.output = output;
        this.outputs = outputs;
        this.actions = actions;
        this.parameters = parameters;;
        this.states = states;
        this.rh = rh;
    }

    public void executeAlgo(GenState s){
        Instance o = new Instance(output, outputs);
        ArrayList<ArrayList<Object>> AOVs = new ArrayList<>();
        for(int i = 0; i < outputs.size(); i++){
            ArrayList<Object> AOV = new ArrayList<>(Arrays.asList(
                    outputs.get(i),
                    actions.get(i),
                    parameters.get(i)
            ));
        }
    }

    @Override
    public Algorithm approx(){
        Algorithm k_a = this;
        //System.out.println("PENIS");

        //PARTIAL APPROX
        for(GenState s : states){
            executeAlgo(s);
        }


        return k_a;
    }
}
