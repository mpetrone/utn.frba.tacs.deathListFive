package com.tacs.deathlist.endpoints;

import javax.ws.rs.GET;
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
        
    	System.out.format("Entró a getUsername en Usuario con: %s%n",username);
    	return Response.status(Response.Status.OK).entity(gsonParser.toJson(username)).build();
    } 
}