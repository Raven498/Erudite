//Could contain meta-knowledge
public class Knowledge {
    protected Agent.KClasses kclass;
    public String name;
    public String content;

    public Knowledge(String name, String content, Agent.KClasses kclass){
        this.name = name;
        this.content = content;
        this.kclass = kclass;
    }
}
