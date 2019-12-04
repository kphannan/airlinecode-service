package com.airline.carrier.controller;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;





import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.airline.carrier.repository.AirlineCodeIcao;
import com.airline.carrier.repository.AirlineCodeIcaoRepository;
import com.airline.core.carrier.ICAOAirlineDesignator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.mockito.InjectMocks;
import org.mockito.Mock;


@SpringBootTest
public class AirlineCodeControllerIcaoTest
{
    // @Autowired
    private MockMvc   mvc;

    // mock repository
    @Mock
    AirlineCodeIcaoRepository repository;

    @InjectMocks
    AirlineCodeControllerIcao    service;

    @BeforeEach
    public void setup()
    {
        mvc = MockMvcBuilders.standaloneSetup(service)
                // .setControllerAdvice(new SuperHeroExceptionHandler())
                // .addFilters(new SuperHeroFilter())
                .build();
    }


    @Test
    public void getSingleKnownIcaoAirlineCode()
        throws Exception
    {
        final ICAOAirlineDesignator airlineCode = new ICAOAirlineDesignator( "LUH" );
        assertThat( airlineCode ).isNotNull();

        given( repository.findById("LUH")).willReturn( Optional.of( new AirlineCodeIcao( airlineCode.getAirlineCode() )));

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/icao/{id}", airlineCode.getAirlineCode() )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk() )
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "LUH")))
            .andReturn().getResponse();
    }


    @Test
    public void getSingleNotKnownIcaoAirlineCode()
        throws Exception
    {
        given( repository.findById("KAL")).willReturn( Optional.ofNullable( null ));

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/icao/{id}", "KAL" )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isNotFound() )
            .andReturn().getResponse();
    }



    @Test
    public void getAllAirlineCodesIsEmpty()
        throws Exception
    {
        given( repository.findAll()).willReturn( Collections.emptyList());

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/icao" ))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk() )
            .andExpect(content().json("[]"))
            .andReturn().getResponse();
    }



    @Test
    public void getAllAirlineCodesHasOneEntry()
        throws Exception
    {
        final String[] codeStringList = { "SNB" };

        // Build a list of ICAO airline code objects from the data layer
        List<AirlineCodeIcao> listOfIcaoAirlineCodes =
            Arrays.asList( codeStringList )
                  .stream()
                  .map( a -> new AirlineCodeIcao( a ))
                  .collect(Collectors.toList());
        
        given( repository.findAll()).willReturn( listOfIcaoAirlineCodes );

        final MockHttpServletResponse response = mvc.perform(
            get("/airline/icao" ))
            .andExpect(status().isOk() )
            .andExpect(content().json("[{icaoAirlineCode: \"SNB\"}]"))
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "SNB")))
            .andReturn().getResponse();
    }



    @Test
    public void deleteKnownAirlineCode()
        throws Exception
    {
        final String targetAirline = "DLH";

        given( repository.findById(targetAirline)).willReturn( Optional.of( new AirlineCodeIcao( targetAirline )));

        final MockHttpServletResponse response = mvc.perform(
            delete("/airline/icao/{id}", targetAirline )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isNoContent() )
            .andReturn().getResponse();
    }

    @Test
    public void deleteAnUnknownAirlineCode()
        throws Exception
    {
        final String targetAirline = "BAW";

        given( repository.findById(targetAirline)).willReturn( Optional.ofNullable( null ));

        final MockHttpServletResponse response = mvc.perform(
            delete("/airline/icao/{id}", targetAirline )
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isNotFound() )
            .andReturn().getResponse();
    }


    @Test
    public void addNewIcaoAirlineCodeAFR()
        throws Exception
    {
        final ICAOAirlineDesignator airlineCode = new ICAOAirlineDesignator( "VIR" );
        assertThat( airlineCode ).isNotNull();

        final String json = "{ \"icaoAirlineCode\": \"AFR\"}";

        AirlineCodeIcao       icao = new AirlineCodeIcao( "AFR" );
        ICAOAirlineDesignator icaoAirlineCode = new ICAOAirlineDesignator("AFR");

        given( repository.save( icao )).willReturn( icao );

        final MockHttpServletResponse response = mvc.perform(
            post("/airline/icao" )
                // .content(icaoAirlineCode)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isCreated() )
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "AFR")))
            .andReturn().getResponse();
    }



    @Test
    public void updateExistingAirlineCode()
        throws Exception
    {
        final String json = "{ \"icaoAirlineCode\": \"VIR\"}";

        AirlineCodeIcao icao = new AirlineCodeIcao( "VIR" );
        ICAOAirlineDesignator icaoAirlineCode = new ICAOAirlineDesignator("VIR");

        given( repository.save( icao ))
            .willReturn( icao );
        given( repository.findById(icao.getIcaoCode()))
            .willReturn( Optional.of( icao ));

        final MockHttpServletResponse response = mvc.perform(
            put("/airline/icao/{id}", icaoAirlineCode.getAirlineCode() )
                // .content(icaoAirlineCode)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk() )
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "VIR")))
            .andReturn().getResponse();
    }

    @Test
    public void updateNonExistingAirlineCodeIsReallyAnAdd()
        throws Exception
    {
        final String json = "{ \"icaoAirlineCode\": \"VIR\"}";

        AirlineCodeIcao icao = new AirlineCodeIcao( "VIR" );
        ICAOAirlineDesignator icaoAirlineCode = new ICAOAirlineDesignator("VIR");

        given( repository.findById(icao.getIcaoCode()))
            .willReturn( Optional.ofNullable(null));
        given( repository.save( icao ))
            .willReturn( icao );

        final MockHttpServletResponse response = mvc.perform(
            put("/airline/icao/{id}", icaoAirlineCode.getAirlineCode() )
                // .content(icaoAirlineCode)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isCreated() )
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "KATL")))
            .andReturn().getResponse();
    }


    @Test
    public void updateAirlineCodeWrongKeyIsInvalid()
        throws Exception
    {
        final String json = "{ \"icaoAirlineCode\": \"VEX\"}";

        AirlineCodeIcao icao = new AirlineCodeIcao( "VIR" );
        ICAOAirlineDesignator icaoAirlineCode = new ICAOAirlineDesignator("VIR");

        given( repository.findById(icao.getIcaoCode()))
            .willReturn( Optional.ofNullable(null));
        given( repository.save( icao ))
            .willReturn( icao );

        final MockHttpServletResponse response = mvc.perform(
            put("/airline/icao/{id}", icaoAirlineCode.getAirlineCode() )
                // .content(icaoAirlineCode)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isBadRequest() )
            // .andExpect( jsonPath("$.icaoAirlineCode", equalTo( "VIR")))
            .andReturn().getResponse();
    }



}

 
 
 