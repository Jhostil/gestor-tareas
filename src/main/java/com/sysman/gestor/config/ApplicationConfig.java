package com.sysman.gestor.config;


import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("com.sysman.gestor.resource");

        register(JacksonFeature.class);
    }
}