package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
 
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test; 

public class UsuarioTest {

    private HttpServer server;
    private WebTarget target; 

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
    public void testGetAllLists() {
        String jsonResponse = target.path("/user/usuario1/list").request().get(String.class);
        
        assertNotNull(jsonResponse);
        assertTrue(!jsonResponse.isEmpty());
    }
    
    @Test
    public void testModUser() {
        Response response = target.path("/user/usuario2").request().post(null);
        
        assertNotNull(response);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
}
