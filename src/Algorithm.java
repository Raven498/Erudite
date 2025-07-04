import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        var n = new ArrayList<AlgoParameters>();
        /*
        n = List<> //List of all unpacked parameters

        process(params):
        if(p.isList()){
            for(AlgoParameters a : p){
                if(!a.isList()){
                    n.add(a);
                    continue;
                }
                process(a);
            }
        }
         */
        return ActionSpace.implement(act, new AlgoParameters(i.getValue(params.get(0).getStr())), new AlgoParameters(i.getValue(params.get(1).getStr())));
    }

    public Instance executeAlgo(GenState s){
        Instance o = new Instance(output);
        for(int i = 0; i < outputs.size(); i++){
            AlgoParameters v = processAct(s.f, actions.get(i), parameters.get(i));
            o.addValue(outputs.get(i), v.getStr());
        }
        return o;
    }

    @Override
    public Algorithm approx(){
        Algorithm k_a = this;

        List<Double> rewards = new ArrayList<>();
        System.out.println(states);

        //PARTIAL APPROX
        for(GenState s : states){
            Instance action = executeAlgo(s);
            rewards.add(rh.reward(s, new GenState(s.name + "-RESPONSE", "I STILL LOVE VALLIUM",  Agent.KClasses.STATE, s.prompt + "-RESPONSE", action)));
        }
        System.out.println(rewards);
        return k_a;
    }
}
