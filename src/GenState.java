public class GenState extends Knowledge{
    public String prompt;
    public Instance f;

    public GenState(String name, String content, Agent.KClasses kclass, String prompt, Instance f){
        super(name, content, kclass);
        this.prompt = prompt;
        this.f = f;
    }
}
