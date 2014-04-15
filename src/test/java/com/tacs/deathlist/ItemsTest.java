package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class ItemsTest {

	private static HttpServer server;
	private static WebTarget target;

	@BeforeClass
	public static void setUp() throws Exception {
		server = Main.startServer();
		Client c = ClientBuilder.newClient();
		target = c.target(Main.BASE_URI);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.shutdownNow();
	}

	@Test
	public void testCreateItem() {
		// TODO: stub
		Response jsonResponse = target.path("/users/aa/lists/bb/items/cc")
				.request(MediaType.APPLICATION_JSON).post(Entity.json(null));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());
	}

	@Test
	public void testVoteItem() {
		// TODO: stub
		Response jsonResponse = target.path("/users/aa/lists/bb/items/cc/vote")
				.request(MediaType.APPLICATION_JSON).put(Entity.json("up"));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());
	}

	@Test
	public void testDeleteItem() {
		// TODO: stub
		Response jsonResponse = target.path("/users/aa/lists/bb/items/cc")
				.request(MediaType.APPLICATION_JSON).delete();

		assertNotNull(jsonResponse);
		assertEquals(Status.OK.getStatusCode(), jsonResponse.getStatus());
	}
}
