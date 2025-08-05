package com.erudite.erudite_node.model;

import com.erudite.erudite_node.service.Agent;

import java.util.ArrayList;

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

    private void unpackAlgoParams(ArrayList<AlgoParameters> algoParams, ArrayList<AlgoParameters> unpacked){
        for(AlgoParameters a : algoParams){
            if(a.isList()){
                unpackAlgoParams(algoParams, unpacked);
                continue;
            }
            unpacked.add(a);
        }
    }

    private AlgoParameters processAct(Instance i, String act, AlgoParameters params){
        return ActionSpace.implement(act, new AlgoParameters(i.getValue(params.get(0).getStr())), new AlgoParameters(i.getValue(params.get(1).getStr())));
    }

    public Instance executeAlgo(GenState s){
        Instance o = new Instance(output);
        ArrayList<AlgoParameters> flatParams = new ArrayList<>();
        unpackAlgoParams(parameters, flatParams);
        for(int i = 0; i < outputs.size(); i++){
            AlgoParameters v = processAct(s.f, actions.get(i), flatParams.get(i)); //TODO: Use HashMap to categorize sets of parameters by action, iterate through this HashMap
            o.addValue(outputs.get(i), v.getStr());
        }
        return o;
    }

    @Override
    public Algorithm approx(){
        Algorithm k_a = this;

        //PARTIAL APPROX

        // ONE: COLLECT ALL REWARDS
        ArrayList<Double> rewards = new ArrayList<>();
        for(GenState s : states){
            Instance o = executeAlgo(s);
            double r = rh.reward(s, new GenState("OUTPUT", o));
            rewards.add(r);
        }

        // TWO: Q-VALUE ANALYSIS

        // THREE: CORRELATION ANALYSIS

        // FOUR: EXPERIMENT

        return k_a;
    }
}
