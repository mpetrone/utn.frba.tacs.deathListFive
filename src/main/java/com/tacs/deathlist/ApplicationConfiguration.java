package com.tacs.deathlist;

import com.tacs.deathlist.endpoint.provider.AuthorizationRequestFilter;
import com.tacs.deathlist.endpoint.provider.GsonMessageBodyHandler;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * 
 * Configuracion de jersey, esta enlazado en el web.xml.
 * 
 * @author matias.petrone
 *
 */
public class ApplicationConfiguration extends ResourceConfig {
    
    public ApplicationConfiguration() {
        setApplicationName("Deathlist!");

        // el packete donde estan nuestros enpoints
        packages("com.tacs.deathlist.endpoint");

        // configuramos el parseo de jersey con gson (tenemos que deshabilitar el que viene por defecto
        property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
        register(GsonMessageBodyHandler.class);

        // configuramos la autenticacion
        register(AuthorizationRequestFilter.class);

        //Para monitoriar la aplicacion desde MBEANS
        //property(ServerProperties.MONITORING_STATISTICS_MBEANS_ENABLED, true);
        
        // Para loguear todos los eventos de entrada y salida (loguea una banda)
        //registerInstances(new LoggingFilter(Logger.getLogger("Deathlist Server"), true));
        
        // El brigde para que slf4j atrape los logs de jersey
        //SLF4JBridgeHandler.removeHandlersForRootLogger();
        //SLF4JBridgeHandler.install();
    }
}
