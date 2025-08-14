package com.erudite.erudite_node;

import com.erudite.erudite_node.management.Director;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class Controller {

    @GetMapping("/algoInfo")
    public AlgoInfo getAlgoInfo(){
        return new AlgoInfo("POWER RULE", "O.a = I.a * I.n, O.x = I.x, O.n = I.n - 1");
    }

    @GetMapping("/instanceTest")
    public void instanceTest() throws IOException {
        System.out.println(Director.getTrueInstance());
    }
}