import java.util.ArrayList;

public class Algorithm extends Knowledge {
    public Concept input;
    public Concept output;
    public ArrayList<String> actions;

    public ArrayList<GenState> train_states;

    public Algorithm(String name, String content, Agent.KClasses kclass, Concept input, Concept output, ArrayList<String> actions) {
        super(name, content, kclass);
        this.input = input;
        this.output = output;
        this.actions = actions;
    }
}
