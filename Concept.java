import java.util.ArrayList;

public class Concept extends Knowledge {
    public String name;
    public ArrayList<String> attr_labels = new ArrayList<>();
    public ArrayList<String> values = new ArrayList<>();

    public Concept(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}
