
import java.util.ArrayList;
import java.util.Arrays;

//TODO: Create general version of this class (using arrays?)

/*
ActionSpace Type Specification:
FOR ALL ACTS: PARAMS MUST BE INTEGER TYPE
 */
public class ActionSpace {
    //Metadata for actions??? (def cooking)
    static ArrayList<String> callbacks = new ArrayList<>(Arrays.asList("ADD", "MULT", "DIV", "NEG"));

    public static AlgoParameters add(int a, int b){
        return new AlgoParameters(a + b);
    }

    public static AlgoParameters mult(int a, int b){
        return new AlgoParameters(a * b);
    }

    public static AlgoParameters div(int a, int b){
        return new AlgoParameters(a / b);
    }

    public static AlgoParameters neg(int a){
        return new AlgoParameters(-a);
    }

    public static AlgoParameters id(int a){return new AlgoParameters(a);}

    public static AlgoParameters implement(String cb, AlgoParameters a, AlgoParameters b){
        return switch (cb) {
            case "ADD" -> add(a.getInt(), b.getInt());
            case "MULT" -> mult(a.getInt(), b.getInt());
            case "DIV" -> div(a.getInt(), b.getInt());
            case "ID" -> id(a.getInt());
            case "NEG" -> neg(a.getInt());
            default -> null;
        };
    }
}
