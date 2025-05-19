import java.util.ArrayList;

public class Instance {
    public Concept c;
    public ArrayList<Object> values = new ArrayList<>();

    public Instance(Concept c, ArrayList<Object> values){
        this.c = c;
        this.values = values;
    }
}
