import java.util.ArrayList;

/*
TODO: Need to create a data type contract for each label that is inherited by Instance
 */
public class Concept extends Knowledge {
    public ArrayList<String> attr_labels;

    public Concept(String name, String content, Agent.KClasses kclass, ArrayList<String> attr_labels) {
        super(name, content, kclass);
        this.attr_labels = attr_labels;
    }
}
