package com.tacs.deathlist;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
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
    private static final String RESOURCES_PATH = "/src/main/resources/com/tacs/deathlist/";

    public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
     * application.
     * @param propertiesManager 
     * @param properties 
     * 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer(PropertiesManager propertiesManager) {
        final ResourceConfig rc = new ResourceConfig().packages("com.tacs.deathlist.endpoints");

        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(propertiesManager.getProperty("base.uri")), rc);

        // agrega un handler para contenido estatico
        // TODO: paths por fuera del jar, cambiar cuando armemos un jar
        HttpHandler staticHandler = new StaticHttpHandler(
                System.getProperty("user.dir") + RESOURCES_PATH + "/client/");

        httpServer.getServerConfiguration().addHttpHandler(staticHandler, "/");

        // deshabilita el cacheo de files, para hacer cambios durante
        // el development
        httpServer.getListener("grizzly").
        getFileCache().setEnabled(new Boolean(propertiesManager.getProperty("file.chache.enable")));

        return httpServer;
    }

    /**
     * Main method.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        configureLog4j();
        PropertiesManager propertiesManager = new PropertiesManager();
        HttpServer server = null;
        try {
            server = startServer(propertiesManager);
            System.in.read();
        } catch (Exception e) {
            LOGGER.error("Hubo un problema con el servidor: " + e);
            if (server != null)
                server.shutdownNow();
        }
    }

    private static void configureLog4j() throws IOException {
        String log4JPropertyFile = System.getProperty("user.dir")
                + RESOURCES_PATH + "properties/log4j.properties";
        Properties p = new Properties();
        p.load(new FileInputStream(log4JPropertyFile));
        PropertyConfigurator.configure(p);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

}
