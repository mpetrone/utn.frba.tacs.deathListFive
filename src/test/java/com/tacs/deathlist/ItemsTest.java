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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ItemsTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		server = Main.startServer();
		Client c = ClientBuilder.newClient();
		target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.shutdownNow();
	}

	@Test
	public void testCreateItem() {
		// TODO: stub
		Response jsonResponse = target.path("/user/aa/list/bb/item/cc")
				.request(MediaType.APPLICATION_JSON).post(Entity.json(null));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());
	}

	@Test
	public void testVoteItem() {
		// TODO: stub
		Response jsonResponse = target.path("/user/aa/list/bb/item/cc")
				.request(MediaType.APPLICATION_JSON).put(Entity.json("up"));

		assertNotNull(jsonResponse);
		assertEquals(Status.CREATED.getStatusCode(), jsonResponse.getStatus());
	}

	@Test
	public void testDeleteItem() {
		// TODO: stub
		Response jsonResponse = target.path("/user/aa/list/bb/item/cc")
				.request(MediaType.APPLICATION_JSON).delete();

		assertNotNull(jsonResponse);
		assertEquals(Status.OK.getStatusCode(), jsonResponse.getStatus());
	}
}
