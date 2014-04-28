package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.tacs.deathlist.domain.Lista;

public class ItemsEndToEndTest {

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

	// Tests

	@Test
        public void crearItemYChequearQueExista() {
        	String item = "perro";
        	
        	createItem(item);
        	Lista lista = getLista();
        	
        	assertNotNull(lista);
        	assertTrue("No se encontro el item perro en la lista", lista.existeElItemConNombre(item));
       
        	
        }

	@Test
	public void crearItemYEliminar() {
    	String item = "gato";
    	
    	createItem(item);
    	deleteItem(item);
    	Lista lista = getLista();
    	
    	assertNotNull(lista);
    	assertTrue("Se encontro el item gato en la lista", !lista.existeElItemConNombre(item));
			
	}

	@Test
	public void crearItemYVotar() {
    	String item = "canario";
    	
    	createItem(item);
    	voteItem(item);

    	Lista lista = getLista();
    	
    	assertNotNull(lista);
    	assertEquals(1, lista.getVotosDelItem(item));
	}

	//Helper Methods

	private void createItem(String item) {	
		List<String> itemsToAdd = new ArrayList<String>();
		itemsToAdd.add(item);
		
		Response jsonResponse = target.path("/users/usuario1/lists/milista1")
				.request(MediaType.APPLICATION_JSON).post(Entity.json(gson.toJson(itemsToAdd)));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());
				
	}	

	private Lista getLista() {
		String response = target.path("/users/usuario1/lists/milista1").request().get(String.class);
        
        assertNotNull(response);
        return gson.fromJson(response, Lista.class);
	}

	private void deleteItem(String item) {
		Response jsonResponse = target.path("/users/usuario1/lists/milista1" + item)
				.request(MediaType.APPLICATION_JSON).delete();

		assertNotNull(jsonResponse);
		assertEquals(Status.OK.getStatusCode(), jsonResponse.getStatus());
						
	}

	private void voteItem(String item) {
		Response jsonResponse = target.path("/users/usuario1/lists/milista1/items/" + item + "/vote")
				.request(MediaType.APPLICATION_JSON).put(Entity.json("up"));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());		
	}

}