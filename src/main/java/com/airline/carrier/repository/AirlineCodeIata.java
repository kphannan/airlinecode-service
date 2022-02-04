package com.airline.carrier.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Declaration of a persistence entity.
 */
@Entity
@Table( name = "IATA_AIRLINECODE" )
@Data
public class AirlineCodeIata
{
    @Id
    @Column( name = "IATA_CODE" )
    private String iataCode;

    protected AirlineCodeIata() {}

    public AirlineCodeIata( String code )
    {
        iataCode = code;
    }
}

