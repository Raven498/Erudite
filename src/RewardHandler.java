/*
Multiple improvements needed to fully generalize this class:
1. Create an internal parsing system for applying other boolean operators on target & transform conditions
2. Support different input vs. output concepts - in target condition, use input concept + instance data, in transform condition, use output concept/instance & input concept/instance

 */

public class RewardHandler {
    private Concept c;
    private double r_y;
    private double r_n;

    /*
    Represents the target condition to be learned - the general conceptual attribute and the value it must equal
    (May have to use boolean cond instead)
     */
    private String attr;
    private String v;

    /*
    This represents information from the chosen output - the boolean expression (inputted by user) represents the transformation
    rule that needs to be followed if the condition was true
     */
    private String a1;
    private String a2;

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
        int target_index = this.c.attr_labels.indexOf(attr);
        int transform_index_o = this.c.attr_labels.indexOf(a2); //NEED TO ADD OUTPUT CONCEPT
        int transform_index_i = this.c.attr_labels.indexOf(a1);
        if(c.f.values.get(target_index) == v && a.f.values.get(transform_index_o) != c.f.values.get(transform_index_i)){
            return r_n;
        }
        return r_y; //In all other conditions, the partial algorithm will guarantee correct reward
    }
}
