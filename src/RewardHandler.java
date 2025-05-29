import java.util.ArrayList;

public class RewardHandler {
    Concept c;
    double r_y;
    double r_n;

    /*
    Represents the target condition to be learned - the general conceptual attribute and the value it must equal
    (May have to use boolean cond instead)
     */
    String attr;
    String v;

    /*
    This represents information from the chosen action - the boolean expression (inputted by user) represents the target transformation
    rule that needs to be followed if the condition was true
     */
    String a1;
    String a2;

    public RewardHandler(double r_y, double r_n, Concept c, String attr, String v, String a1, String a2){
        this.c = c;
        this.r_y = r_y;
        this.r_n = r_n;
        this.attr = attr;
        this.v = v;
        this.a1 = a1;
        this.a2 = a2;
    }

    /*
    The "action" is an output state returned by the agent representing the current algorithm's outputs to be improved
     */
    public double reward(GenState c, GenState a){
        /*
        Punishment Condition:
        Checks if:
        1) the target condition is true
        2) the transform condition is false (the agent did not conduct target transform)
         */
        int index = this.c.attr_labels.indexOf(attr);
        if(c.f.values.get(index) == v && a.f.values.get(index) != c.f.values.get(index)){
            return r_n;
        }
        return r_y; //In all other conditions, the partial algorithm will guarantee correct reward
    }
}
