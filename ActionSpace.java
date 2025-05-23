import java.util.ArrayList;
import java.util.Arrays;

//TODO: Create general version of this class (using arrays?)
public class ActionSpace {
    //Metadata for actions??? (def cooking)
    static int v;
    static ArrayList<String> callbacks = new ArrayList<>(Arrays.asList("ADD", "MULT", "DIV", "NEG"));

    public static int add(int a, int b){
        return a + b;
    }

    public static int mult(int a, int b){
        return a * b;
    }

    public static int div(int a, int b){
        return a / b;
    }

    public static int neg(int a){
        return -a;
    }

    public static void implement(String cb, int a, int b){
        switch(cb){
            case "ADD":
                v = add(a, b);

            case "MULT":
                v = mult(a, b);

            case "DIV":
                v = div(a, b);

            case "NEG":
                v = neg(a);
        }
    }
}
