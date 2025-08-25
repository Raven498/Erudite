package com.erudite.erudite_node.dev_db;

import jakarta.persistence.*;

@Entity
public class Pod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="ip", nullable = false)
    private String ip;

    @Column(name="approx")
    private String approx;

    public String getIp(){
        return ip;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public String getApprox(){return approx;}
    
    public void setApprox(String approx){this.approx = approx;}
}
