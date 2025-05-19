public class GenState extends Knowledge{
    String prompt;
    Instance f;

    public GenState(String prompt, Instance f){
        this.f = f;
    }
}
