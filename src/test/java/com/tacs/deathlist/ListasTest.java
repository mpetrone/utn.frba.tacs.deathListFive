package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class ListasTest {

    private HttpServer server;
    private WebTarget target;
    private Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }


    @Test
    public void testGetLists() {
        String jsonResponse = target.path("/users/usuario1/list/lista1").request().get(String.class);
        
        assertNotNull(jsonResponse);
        assertTrue(!jsonResponse.isEmpty());
        List<?> list = gson.fromJson(jsonResponse, List.class);
        assertEquals(3, list.size());
    }
}
