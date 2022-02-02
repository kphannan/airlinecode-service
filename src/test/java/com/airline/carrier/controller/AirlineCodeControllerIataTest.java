package com.airline.carrier.controller;


// import static org.assertj.core.api.Assertions.*;

import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.airline.carrier.repository.AirlineCodeIata;
import com.airline.carrier.repository.AirlineCodeIataRepository;
import com.airline.core.carrier.IATAAirlineDesignator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.mockito.InjectMocks;
import org.mockito.Mock;


@SpringBootTest
public class AirlineCodeControllerIataTest
{
    // @Autowired
    private MockMvc   mvc;

    // mock repository
    @Mock
    AirlineCodeIataRepository repository;

    @InjectMocks
    AirlineCodeControllerIata    service;

    @BeforeEach
    public void setup()
    {
        mvc = MockMvcBuilders.standaloneSetup( service )
                // .setControllerAdvice(new SuperHeroExceptionHandler())
                // .addFilters(new SuperHeroFilter())
                .build();
    }


    @Test
    public void getSingleKnownIataAirlineCode()
        throws Exception
    {
        final IATAAirlineDesignator airlineCode = new IATAAirlineDesignator( "1Z" );

        given( repository.findById( "1Z" ) ).willReturn( Optional.of( new AirlineCodeIata( airlineCode.getAirlineCode() ) ) );

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/iata/{id}", airlineCode.getAirlineCode() )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk() )
            // .andExpect( jsonPath("$.iataAirlineCode", equalTo( "1Z")))
            .andReturn().getResponse();
    }


    @Test
    public void getSingleNotKnownIataAirlineCode()
        throws Exception
    {
        given( repository.findById("DV")).willReturn( Optional.ofNullable( null ));

        final MockHttpServletResponse response =
            mvc.perform( get( "/airline/iata/{id}", "DV" )
                            .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andReturn()
                .getResponse();
    }



    @Test
    public void getAllAirlineCodesIsEmpty()
        throws Exception
    {
        given( repository.findAll()).willReturn( Collections.emptyList());

        final MockHttpServletResponse response =
            mvc.perform( get( "/airline/iata" ) )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
                .andExpect( content().json( "[]") )
                .andReturn()
                .getResponse();
    }



    @Test
    public void getAllAirlineCodesHasOneEntry()
        throws Exception
    {
        final String[] codeStringList = { "DL" };

        // Build a list of IATA airline code objects from the data layer
        List<AirlineCodeIata> listOfIataAirlineCodes =
            Arrays.asList( codeStringList )
                  .stream()
                  .map( a -> new AirlineCodeIata( a ))
                  .collect(Collectors.toList());

        given( repository.findAll()).willReturn( listOfIataAirlineCodes );

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/iata" ))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk() )
            .andExpect(content().json("[{iataAirlineCode: \"DL\"}]"))
            // .andExpect( jsonPath("$.iataAirlineCode", equalTo( "DL")))
            .andReturn().getResponse();
    }



    @Test
    public void deleteKnownAirlineCode()
        throws Exception
    {
        final String targetAirline = "DL";

        given( repository.findById(targetAirline)).willReturn( Optional.of( new AirlineCodeIata( targetAirline )));

        final MockHttpServletResponse response = mvc.perform(
            delete("/airline/iata/{id}", targetAirline )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isNoContent() )
            .andReturn().getResponse();
    }

    @Test
    public void deleteAnUnknownAirlineCode()
        throws Exception
    {
        final String targetAirline = "ZZ";

        given( repository.findById(targetAirline)).willReturn( Optional.ofNullable( null ));

        final MockHttpServletResponse response = mvc.perform(
            delete("/airline/iata/{id}", targetAirline )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isNotFound() )
            .andReturn().getResponse();
    }


    @Test
    public void addNewIataAirlineCodeLH()
        throws Exception
    {
        final IATAAirlineDesignator airlineCode = new IATAAirlineDesignator( "LH" );

        final String json = "{ \"iataAirlineCode\": \"LH\"}";

        AirlineCodeIata iata = new AirlineCodeIata( "LH" );

        given( repository.save( iata )).willReturn( iata );

        final MockHttpServletResponse response = mvc.perform(
            post("/airline/iata" )
                // .content(iataAirlineCode)
                .content( json )
                .contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON ) )
            .andExpect(status().isCreated() )
            // .andExpect( jsonPath("$.iataAirlineCode", equalTo( "LH")))
            .andReturn().getResponse();
    }



    @Test
    public void updateExistingAirlineCode()
        throws Exception
    {
        final String json = "{ \"iataAirlineCode\": \"DL\"}";

        AirlineCodeIata iata = new AirlineCodeIata( "DL" );
        IATAAirlineDesignator iataAirlineCode = new IATAAirlineDesignator("DL");

        given( repository.save( iata ))
            .willReturn( iata );
        given( repository.findById( iata.getIataCode() ) )
            .willReturn( Optional.of( iata ) );

        final MockHttpServletResponse response = mvc.perform(
            put("/airline/iata/{id}", iataAirlineCode.getAirlineCode() )
                // .content(iataAirlineCode)
                .content( json )
                .contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON ) )
            .andExpect(status().isOk() )
            // .andExpect( jsonPath("$.iataAirlineCode", equalTo( "DL")))
            .andReturn().getResponse();
    }

    @Test
    public void updateNonExistingAirlineCodeIsReallyAnAdd()
        throws Exception
    {
        final String json = "{ \"iataAirlineCode\": \"GH\"}";

        AirlineCodeIata iata = new AirlineCodeIata( "GH" );
        IATAAirlineDesignator iataAirlineCode = new IATAAirlineDesignator( "GH" );

        given( repository.findById(iata.getIataCode() ) )
            .willReturn( Optional.ofNullable( null ) );
        given( repository.save( iata ) )
            .willReturn( iata );

        final MockHttpServletResponse response = mvc.perform(
            put("/airline/iata/{id}", iataAirlineCode.getAirlineCode() )
                // .content(iataAirlineCode)
                .content( json )
                .contentType( MediaType.APPLICATION_JSON )
                .accept( MediaType.APPLICATION_JSON ) )
            .andExpect(status().isCreated() )
            // .andExpect( jsonPath("$.iataAirlineCode", equalTo( "GH")))
            .andReturn()
            .getResponse();
    }


    @Test
    public void updateAirlineCodeWrongKeyIsInvalid()
        throws Exception
    {
        final String json = "{ \"iataAirlineCode\": \"LX\"}";

        AirlineCodeIata iata = new AirlineCodeIata( "ZZ" );
        IATAAirlineDesignator iataAirlineCode = new IATAAirlineDesignator( iata.getIataCode() );

        given( repository.findById( iata.getIataCode() ) )
            .willReturn( Optional.ofNullable( null ) );
        given( repository.save( iata ) )
            .willReturn( iata );

        final MockHttpServletResponse response =
            mvc.perform( put("/airline/iata/{id}", iataAirlineCode.getAirlineCode() )
                            // .content(iataAirlineCode)
                            .content( json )
                            .contentType( MediaType.APPLICATION_JSON )
                            .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() )
                .andReturn()
                .getResponse();
    }



}



