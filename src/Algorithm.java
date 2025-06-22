import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm extends Knowledge {
    public ArrayList<GenState> states;
    public Concept input;
    public Concept output;
    public ArrayList<String> outputs;
    public ArrayList<String> actions;
    public ArrayList<AlgoParameters> parameters;
    public RewardHandler rh;

    public Algorithm(String name, String content, Agent.KClasses kclass, Concept input, Concept output, ArrayList<String> outputs, ArrayList<String> actions, ArrayList<AlgoParameters> parameters, ArrayList<GenState> states, RewardHandler rh) {
        super(name, content, kclass);
        this.input = input;
        this.output = output;
        this.outputs = outputs;
        this.actions = actions;
        this.parameters = parameters;;
        this.states = states;
        this.rh = rh;
    }

    private AlgoParameters processAct(Instance i, String act, AlgoParameters params){
        return ActionSpace.implement(act, new AlgoParameters(i.getValue(params.get(0).getStr())), new AlgoParameters(i.getValue(params.get(1).getStr())));
    }

    public void executeAlgo(GenState s){
        Instance o = new Instance(output);
        for(int i = 0; i < outputs.size(); i++){
            AlgoParameters v = processAct(s.f, actions.get(i), parameters.get(i));
            o.addValue(outputs.get(i), v.getStr());
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
