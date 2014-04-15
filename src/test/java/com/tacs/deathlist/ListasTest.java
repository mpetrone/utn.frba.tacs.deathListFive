package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

public class ListasTest {

    private static HttpServer server;
    private static WebTarget target;
    private Gson gson = new Gson();

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
    public void testGetAllLists() {
        String jsonResponse = target.path("/users/usuario1/lists").request().get(String.class);
        
        assertNotNull(jsonResponse);
        assertTrue(!jsonResponse.isEmpty());
        List<?> list = gson.fromJson(jsonResponse, List.class);
        assertEquals(1, list.size());
    }

    @Test
    public void testGetLists() {
        String jsonResponse = target.path("/users/usuario1/lists/lista1").request().get(String.class);
        
        assertNotNull(jsonResponse);
        assertTrue(!jsonResponse.isEmpty());
        List<?> list = gson.fromJson(jsonResponse, List.class);
        assertEquals(3, list.size());
    }
    
    @Test
    public void testCreateLists() {
        Response response = target.path("/users/usuario1/lists/lista1").request().post(null);
        
        assertNotNull(response);
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteLists() {
        Response response = target.path("/users/usuario1/lists/lista1").request().delete();
        
        assertNotNull(response);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
}
