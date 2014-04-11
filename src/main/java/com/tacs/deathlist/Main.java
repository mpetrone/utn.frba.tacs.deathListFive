package com.tacs.deathlist;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI en donde vamos a estar escuchando
    public static final String BASE_URI = "http://localhost:8080/deathlist/";

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
        HttpServer server = null;
        try {
            server = startServer();
            System.in.read();
        } catch (Exception e) {
            System.out.println("Hubo un problema con el servidor: " + e);
            if(server != null)
                server.shutdownNow();
        }
    }
}

