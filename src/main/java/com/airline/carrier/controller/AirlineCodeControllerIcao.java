package com.airline.carrier.controller;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

import com.airline.carrier.repository.AirlineCodeIcao;
import com.airline.carrier.repository.AirlineCodeIcaoRepository;
import com.airline.core.carrier.AirlineCode;
import com.airline.core.carrier.ICAOAirlineDesignator;
import com.airline.core.carrier.AirlineCodeFactory;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/airline/icao")
public class AirlineCodeControllerIcao {
    private final AirlineCodeIcaoRepository repository;

    AirlineCodeControllerIcao(AirlineCodeIcaoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    ResponseEntity<List<AirlineCode>> all() {
        List<AirlineCodeIcao> result = repository.findAll();

        if (result.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Map the results to Domain Objects rather than DB objects
        List<AirlineCode> airports = result.stream().map(iata -> new ICAOAirlineDesignator(iata.getIcaoCode()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(airports);
    }

    @PostMapping("")
    ResponseEntity<AirlineCode> newAirlineCode(@RequestBody ICAOAirlineDesignator newAirlineCode) {
        AirlineCodeIcao icaoDB = repository.save(new AirlineCodeIcao(newAirlineCode.getAirlineCode()));

        // Return 201 (Created)
        return new ResponseEntity<>(new ICAOAirlineDesignator(icaoDB.getIcaoCode()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<AirlineCode> one(@PathVariable String id) {
        Optional<AirlineCodeIcao> iao = repository.findById(id);
        if (iao.isPresent())
            return ResponseEntity.ok(AirlineCodeFactory.build(iao.get().getIcaoCode()));
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<AirlineCode> replaceAirlineCode( @RequestBody ICAOAirlineDesignator newAirlineCode,
                                                    @PathVariable String id)
    {
        if ( !id.equals( newAirlineCode.getAirlineCode()))
        return ResponseEntity.badRequest().build();

        Optional<AirlineCodeIcao> icao = repository.findById(id);
        if (icao.isPresent()) {
            return ResponseEntity.ok( newAirlineCode );
        }

        AirlineCodeIcao zzz = new AirlineCodeIcao( newAirlineCode.getAirlineCode() );
        repository.save( zzz );

        return ResponseEntity.status(HttpStatus.CREATED).body( newAirlineCode );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> deleteAirlineCode(@PathVariable String id)
    {
        Optional<AirlineCodeIcao> icao = repository.findById(id);
        if ( icao.isPresent() )
        {
            repository.deleteById(id);  // throws IllegalArgumentException if ID not found...
            return ResponseEntity.noContent().build();   // HTTP 204
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

}
