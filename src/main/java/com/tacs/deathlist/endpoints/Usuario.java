package com.tacs.deathlist.endpoints;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/users/{username}")
public class Usuario {

	private List<Listas> allLists = new ArrayList<>();
	private Gson gsonParser = new Gson();

	// Recupera username. [?]
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsername(@PathParam("username") String username) {

		System.out.format("Entró a getUsername en Usuario con: %s%n", username);
		return Response.status(Response.Status.OK)
				.entity(gsonParser.toJson(username)).build();
	}

	// Recupera todas las listas de un user.
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllLists(@PathParam("username") String username) {
		System.out.format("Entró a getAllLists en Usuario con: %s%n", username);
		return Response.status(Response.Status.OK)
				.entity(gsonParser.toJson(allLists)).build();
	}

	// Modifica user.
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response modUser(@PathParam("username") String username) {
		System.out.format("Entró a modUser en Usuario con: %s%n", username);
		return Response.status(Response.Status.OK).build();
	}

}