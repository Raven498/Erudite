import java.util.ArrayList;

public class Environment {
    private ArrayList<Knowledge> knowledge = new ArrayList<>();

    public ArrayList<Knowledge> getKnowledge(){
        return knowledge;
    }

    public void addKnowledge(Knowledge k){
        knowledge.add(k);
    }
}
