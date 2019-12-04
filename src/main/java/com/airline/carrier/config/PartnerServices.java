package com.airline.carrier.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;
// import lombok.Value;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * This is the partner service configuration.
 *
 */
@ConfigurationProperties(prefix = "partner-services")
@Component
@RefreshScope
@Data
public class PartnerServices
{
    private String                foo;
    private String                channelId;        //< default channel ID
    private String                applicationId;    //< default application id
    private List<ServiceInfo>     services;
    private List<SoapServiceInfo> soapServices;


    @PostConstruct
    public void init()
    {
        // Build out the transient elements....
        soapServices.forEach(svc -> {
            svc.uri = (svc.certificate == null ? "http://" : "https://") + svc.host + ":" + svc.port;
        });

        System.out.println( "----- Examine the parter service list -----");
        // System.out.println(services);
        // System.out.println( something );
        // System.out.println( foo );
        System.out.println("   -- Services --");
        services.forEach(System.out::println);

        System.out.println("   -- SOAP Services --");
        soapServices.forEach(System.out::println);

        System.out.println("----- End of service configuration -----");
    }


    /**
     *
     */
    @Data
    @Setter(AccessLevel.MODULE)
    public static class ServiceInfo
    {
        private String       name;
        private String       host;
        private String       healthEndpoint;
        private ClientPolicy clientPolicy;
    }

    /**
     *
     */
    @Data
    public static class SoapServiceInfo
    {
        @Setter(AccessLevel.MODULE)
        private String          name;           //< Name of the service
        @Setter(AccessLevel.MODULE)
        private String          host;           //< host only of the URL
        @Setter(AccessLevel.MODULE)
        private String          port;           //< port number
        @Setter(AccessLevel.MODULE)
        private CertificateInfo certificate;    //< Optional certificate keystore
        @Setter(AccessLevel.MODULE)
        private ClientPolicy    clientPolicy;   //< Optional policy details (timeouts, retries, etc.)

        @Setter(AccessLevel.NONE)
        private transient String uri;           //< Constructed URI from host, port, certificate details
    }

    /**
     *
     */
    @Data
    @Setter(AccessLevel.MODULE)
    public static class CertificateInfo
    {
        private String    keystoreFilename;     //< filename (no path) of the keystore
        private String    username;             //< keystore credentials
        private String    paassword;            // < keystore credentials
    }

    /**
     *
     */
    @Data
    @Setter(AccessLevel.MODULE)
    public static class ClientPolicy
    {
        private int      connectTimeout;    //< max milliseconds to establish a connection
        private int      maxRetries;        //< count of retries
        private int      timeout;           //< timeout in ms; zero (0) results in no timeout
        private int      maxElapsedTime;    //< maximum elapsed time from first attempt in ms.
        private boolean  useAsyncStrategy;  //< if true, calls are async otherwise synchronous

    }
}


