package com.airline.carrier.controller;




public class AirlineCodeNotFoundException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    AirlineCodeNotFoundException(String id)
    {
        super( "Could not find airline code ''" + id  + "'" );
    }
}
