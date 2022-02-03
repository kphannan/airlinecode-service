package com.airline.carrier;

import com.intuit.karate.junit5.Karate;

// import lombok.extern.log4j.Log4j2;


// @Log4j2
public class KarateRunnerIT {

    @Karate.Test
    public Karate testAll() {
        // log.info( "Run Karate Integration Tests" );
        System.out.println( "Run Karate Integration Tests" );

        return new Karate().relativeTo(getClass());
    }

}