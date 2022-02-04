package com.airline.carrier.controller;




/**
 * Exception indicating an airline code was not found in the persistent store.
 */
public class AirlineCodeNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    AirlineCodeNotFoundException( String id )
    {
        super( "Could not find airline code ''" + id  + "'" );
    }
}
