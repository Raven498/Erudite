import java.util.ArrayList;

public class Algorithm extends Knowledge {
    public Concept input;
    public Concept output;
    public String action;

    public ArrayList<GenState> train_states;

    public Algorithm(String name, String content, Agent.KClasses kclass, Concept input, Concept output, ArrayList<GenState> train_states) {
        super(name, content, kclass);
        this.input = input;
        this.output = output;
        this.train_states = train_states;
    }
}
