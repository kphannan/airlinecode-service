package com.airline.carrier;

import com.intuit.karate.junit5.Karate;

// import lombok.extern.log4j.Log4j2;


/**
 * Main BDD test runner.
 */
// @Log4j2
public class KarateRunnerIT
{
    /**
     * Executes all feature files found in this package and below.
     *
     * @return
     */
    @Karate.Test
    public Karate testAll()
    {
        // log.info( "Run Karate BDD Tests" );
        // System.out.println( "Run Karate BDD Tests" );

        return new Karate().relativeTo( getClass() );
    }

}