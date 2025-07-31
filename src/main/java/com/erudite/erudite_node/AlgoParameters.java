package com.erudite.erudite_node;
import java.util.ArrayList;
import java.util.List;

public class AlgoParameters{
    private String p1;
    private List<AlgoParameters> p2;
    private int p3;
    public AlgoParameters(String p1){
        this.p1 = p1;
    }

    public AlgoParameters(List<AlgoParameters> p2){
        this.p2 = p2;
    }

    public AlgoParameters(int p3){
        this.p3 = p3;
    }

    public AlgoParameters get(int index){
        return p2.get(index);
    }

    public String getStr(){
        return p1;
    }

    public int getInt(){
        return p3;
    }

    public boolean isList(){
        return p2 == null;
    }
}
