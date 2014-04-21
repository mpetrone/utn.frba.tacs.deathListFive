package com.tacs.deathlist;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI en donde vamos a estar escuchando
    public static final String BASE_URI = "http://localhost:8080/deathlist/";
    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        //agarra las clases del paquete com.tacs.deathlist e inicia el servidor
        final ResourceConfig rc = new ResourceConfig().packages("com.tacs.deathlist.endpoints");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        configureLog4j();
        HttpServer server = null;
        try {
            server = startServer();
            System.in.read();
        } catch (Exception e) {
            LOGGER.error("Hubo un problema con el servidor: " + e);
            if(server != null)
                server.shutdownNow();
        }
    }

    private static void configureLog4j() throws IOException{
        String log4JPropertyFile = System.getProperty( "user.dir") + "/src/main/resources/com/tacs/deathlist/properties/log4j.properties";
        Properties p = new Properties();
        p.load(new FileInputStream(log4JPropertyFile));
        PropertyConfigurator.configure(p);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}

