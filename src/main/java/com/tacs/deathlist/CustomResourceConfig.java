package com.tacs.deathlist;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;

import com.tacs.deathlist.endpoints.providers.GsonMessageBodyHandler;

public class CustomResourceConfig extends Application {
   
    public static ResourceConfig rc = new ResourceConfig().packages("com.tacs.deathlist.endpoints")
    .register(GsonMessageBodyHandler.class).property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);

    @Override
    public Set<Class<?>> getClasses() {
        return rc.getClasses();
    }

    @Override
    public Map<String, Object> getProperties() {
        return rc.getProperties();
    }
    
    @Override
    public Set<Object> getSingletons() {
        return rc.getSingletons();
    }
}
