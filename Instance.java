import java.util.ArrayList;

public class Instance {
    public Concept c;
    public ArrayList<Integer> values = new ArrayList<>();

    public Instance(Concept c, ArrayList<Integer> values){
        this.c = c;
        this.values = values;
    }
}
