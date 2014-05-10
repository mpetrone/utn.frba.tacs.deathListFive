package com.tacs.deathlist;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ApplicationRunner for test purpose.
 * 
 */
public class ApplicationRunner {
    public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunner.class);

    public static HttpServer startServer() throws IOException {
        PropertiesManager propertiesManager = new PropertiesManager();
        HttpServer server = null;
        try {
            server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(propertiesManager.getProperty("base.uri")), CustomResourceConfig.rc);
        } catch (Exception e) {
            LOGGER.error("Hubo un problema con el servidor: " + e);
            if (server != null)
                server.shutdownNow();
        }
        return server;
    }
}
