package com.airline.carrier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

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

