import java.util.ArrayList;

//Currently only models univariate relationships

public class Relationship extends Knowledge {
    private double x;
    private double w;
    private double b;

    public Relationship(String name, String content, Agent.KClasses kclass) {
        super(name, content, kclass);
    }
}