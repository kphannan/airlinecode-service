package com.airline.carrier.config;

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
// import lombok.Value;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * This is the partner service configuration.
 *
 */
@ConfigurationProperties( prefix = "partner-services" )
@Component
@RefreshScope
@Data
@Log4j2
public class PartnerServices
{
    private String                foo;
    private String                channelId;        //< default channel ID
    private String                applicationId;    //< default application id
    private List<ServiceInfo>     services;
    private List<SoapServiceInfo> soapServices;

    /**
     * Process the configration and build URLs for individual services.
     */
    @PostConstruct
    private void init()
    {
        log.info( "----- Examine the parter service list -----" );
        log.info( "   -- Services --" );
        if ( null != services )
        {
            services.forEach( log::info );
        }

        log.info( "   -- SOAP Services --" );
        // Build out the transient elements....
        if ( null != soapServices )
        {
            soapServices.forEach( svc ->
            {
                svc.uri = ( svc.certificate == null ? "http://" : "https://" ) + svc.host + ":" + svc.port;
            } );

            soapServices.forEach( log::info );
        }

        log.info( "----- End of service configuration -----" );
    }


    /**
     * Identification of a REST service endpoint.
     */
    @Data
    @Setter( AccessLevel.MODULE )
    public static class ServiceInfo
    {
        private String       name;
        private String       host;
        private String       healthEndpoint;
        private ClientPolicy clientPolicy;
    }

    /**
     * Identification of a SOAP service endpoint.
     */
    @Data
    public static class SoapServiceInfo
    {
        @Setter( AccessLevel.MODULE )
        private String          name;           //< Name of the service
        @Setter( AccessLevel.MODULE )
        private String          host;           //< host only of the URL
        @Setter( AccessLevel.MODULE )
        private String          port;           //< port number
        @Setter( AccessLevel.MODULE )
        private CertificateInfo certificate;    //< Optional certificate keystore
        @Setter( AccessLevel.MODULE )
        private ClientPolicy    clientPolicy;   //< Optional policy details (timeouts, retries, etc.)

        @Setter( AccessLevel.NONE )
        private transient String uri;           //< Constructed URI from host, port, certificate details
    }

    /**
     * Security credentials for a keystore necessary to obtain a certificate from the keystore.
     */
    @Data
    @Setter( AccessLevel.MODULE )
    public static class CertificateInfo
    {
        private String    keystoreFilename;     //< filename (no path) of the keystore
        private String    username;             //< keystore credentials
        private String    paassword;            // < keystore credentials
    }

    /**
     * Characteristics on how to interact with a service.
     */
    @Data
    @Setter( AccessLevel.MODULE )
    public static class ClientPolicy
    {
        private int      connectTimeout;    //< max milliseconds to establish a connection
        private int      maxRetries;        //< count of retries
        private int      timeout;           //< timeout in ms; zero (0) results in no timeout
        private int      maxElapsedTime;    //< maximum elapsed time from first attempt in ms.
        private boolean  useAsyncStrategy;  //< if true, calls are async otherwise synchronous\
    }
}


