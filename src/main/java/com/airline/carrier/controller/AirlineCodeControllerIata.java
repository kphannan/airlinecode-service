package com.airline.carrier.controller;


import com.airline.carrier.repository.AirlineCodeIata;
import com.airline.carrier.repository.AirlineCodeIataRepository;
import com.airline.core.carrier.AirlineCode;
import com.airline.core.carrier.AirlineCodeFactory;
import com.airline.core.carrier.IATAAirlineDesignator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;






/**
 * REST controller for IATA style airline codes.
 */
@RestController
@RequestMapping( "/airline/iata" )
public class AirlineCodeControllerIata
{
    private final AirlineCodeIataRepository     repository;

    AirlineCodeControllerIata( AirlineCodeIataRepository repository )
    {
        this.repository = repository;
    }


    @GetMapping( "" )
    ResponseEntity<List<AirlineCode>> all()
    {
        // Get list of airline codes from the DB, potentially an empty list.
        List<AirlineCodeIata> result = repository.findAll();

        if ( result.isEmpty() )
        {
            return ResponseEntity.ok( Collections.emptyList() );
        }

        // Map the results to Domain Objects rather than DB objects
        List<AirlineCode> airlines =
            result.stream()
                  .map( iata -> new IATAAirlineDesignator( iata.getIataCode() ) )
                  .collect( Collectors.toList() );

        return ResponseEntity.ok( airlines );
    }

    @PostMapping( "" )
    ResponseEntity<AirlineCode> newAirlineCode( @RequestBody IATAAirlineDesignator newAirlineCode )
    {
        AirlineCodeIata iataDB = repository.save( new AirlineCodeIata( newAirlineCode.getAirlineCode() ) );

        // Return 201 (Created)
        return ResponseEntity.status( HttpStatus.CREATED ).body( new IATAAirlineDesignator( iataDB.getIataCode() ) );
    }








    @GetMapping( "/{id}" )
    ResponseEntity<AirlineCode> one( @PathVariable String id )
    {
        Optional<AirlineCodeIata> iata = repository.findById( id );
        if ( iata.isPresent() )
        {
            return ResponseEntity.ok( AirlineCodeFactory.build( iata.get().getIataCode() ) );
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping( "/{id}" )
    ResponseEntity<AirlineCode> replaceAirlineCode( @RequestBody IATAAirlineDesignator newAirlineCode, @PathVariable String id )
    {
        if ( !id.equals( newAirlineCode.getAirlineCode() ) )
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<AirlineCodeIata> iata = repository.findById( id );
        if ( iata.isPresent() )
        {
            return ResponseEntity.ok( newAirlineCode );
        }

        AirlineCodeIata zzz = new AirlineCodeIata( newAirlineCode.getAirlineCode() );
        repository.save( zzz );
        // Map the response to a Domain Object
        // System.out.println( "save()");
        // return new ResponseEntity<>( new IATAAirlineDesignator( repository.save( newAirlineCode ).getIataCode()),
        //                              HttpStatus.CREATED );
        return ResponseEntity.status( HttpStatus.CREATED ).body( newAirlineCode );
    }

    @DeleteMapping( "/{id}" )
    ResponseEntity<Boolean> deleteAirlineCode( @PathVariable String id )
    {
        Optional<AirlineCodeIata> iata = repository.findById( id );
        if ( iata.isPresent() )
        {
            repository.deleteById( id );  // throws IllegalArgumentException if ID not found...
            return ResponseEntity.noContent().build();   // HTTP 204
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

}
