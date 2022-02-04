package com.airline.carrier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Main entry point of the REST Service.
 */
@Slf4j
@SpringBootApplication
// @EnableCaching
// @EnableZuulProxy
// @EnableFeignClients
public class AirlineCodeApplication
{

    public static void main( String[] args )
    {
        log.info( "Starting the app" );
        SpringApplication.run( AirlineCodeApplication.class, args );
    }

}

