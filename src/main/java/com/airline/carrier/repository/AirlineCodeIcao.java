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
@Table( name = "ICAO_AIRLINECODE" )
@Data
public class AirlineCodeIcao
{
    @Id
    @Column( name = "ICAO_CODE" )
    private String icaoCode;

    protected AirlineCodeIcao() {}

    public AirlineCodeIcao( String code )
    {
        icaoCode = code;
    }
}

