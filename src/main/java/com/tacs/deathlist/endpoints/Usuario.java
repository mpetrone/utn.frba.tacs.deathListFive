package com.tacs.deathlist.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/users/{username}")
public class Usuario {

    private Gson gsonParser = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsername(@PathParam("username") String username) {
        System.out.println("Entró a getUsername en Usuario con " + username);
        return Response.status(Response.Status.OK)
                .entity(gsonParser.toJson(username)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("username") String username) {
        System.out.println("Entró a createUser en Usuario con " + username);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyUser(@PathParam("username") String username) {
        System.out.println("Entró a modifyUser en Usuario con " + username);
        return Response.status(Response.Status.OK).build();
    }

}