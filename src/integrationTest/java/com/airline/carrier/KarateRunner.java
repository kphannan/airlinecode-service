package com.airline.carrier;

import com.intuit.karate.junit5.Karate;

public class KarateRunner {

    @Karate.Test
    public Karate testAll() {
        System.out.println( "Run Karate Integration Tests");
        return new Karate().relativeTo(getClass());
    }
    
}