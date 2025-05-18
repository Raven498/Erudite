public class State {
    private double target;
    private double feature;

    public State(double target, double feature){
        this.target = target;
        this.feature = feature;
    }

    public double getTarget(){
        return target;
    }

    public double getFeature(){
        return feature;
    }
}
