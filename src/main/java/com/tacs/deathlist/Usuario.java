package com.tacs.deathlist;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;


@Path("/users/{username}")
public class Usuario {
    
	@PathParam("username") 
	private String username;
	
    public Usuario() {
    	
    }

    @GET
    @Produces("text/plain")
    public String getUsername() { 
        
    	System.out.format("Entró a getUsername en Usuario con: %s%n",username);
    	return username;
    } 
    
    @GET @Path("/lists/{listName}")
    @Produces("text/plain")
    public Response getList(@PathParam("listName") String listName) { 
        
    	System.out.format("Entró a getList en Usuario con: %s%n",listName);
    	return Response.status(Response.Status.OK).build();
    }  
    
    @POST @Path("/lists/{listName}")
    @Consumes("text/plain")
    public void createList(@PathParam("listName") String listName) { 
        
    	System.out.format("Entró a createList en Usuario con: %s%n",listName);
    }    
    
}
