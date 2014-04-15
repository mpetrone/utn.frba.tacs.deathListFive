package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UsuarioTest {

    private static HttpServer server;
    private static WebTarget target; 

    @BeforeClass
    public static void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdownNow();
    }
    
    @Test
    public void testModUser() {
        Response response = target.path("/users/usuario1").request().get();
        
        assertNotNull(response);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testCreateUser() {
        Response response = target.path("/users/usuario1").request().post(null);
        
        assertNotNull(response);
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testModifyUser() {
        Response response = target.path("/users/usuario1").request().put(Entity.json("modify the user!"));
        
        assertNotNull(response);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
}
