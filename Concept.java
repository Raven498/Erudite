import java.util.ArrayList;

public class Concept extends Knowledge {
    public String name;
    public ArrayList<String> attr_labels;

    public Concept(String name, String content, Agent.KClasses kclass, ArrayList<String> attr_labels) {
        super(name, content, kclass);
        this.attr_labels = attr_labels;
    }
}
