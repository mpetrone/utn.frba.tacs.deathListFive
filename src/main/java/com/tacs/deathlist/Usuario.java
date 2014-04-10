package com.tacs.deathlist;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

// TODO: yo lo haría asi: @Path("/users/{username}")
@Path("/{username}")
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
    
 // TODO: yo lo haría asi: @GET @Path("/lists/{listName}")
    @GET @Path("/list/{listName}")
    @Produces("text/plain")
    public Response getList(@PathParam("listName") String listName) { 
        
    	System.out.format("Entró a getList en Usuario con: %s%n",listName);
    	return Response.status(Response.Status.OK).build();
    }  
    
    // TODO: yo lo haría asi: @POST @Path("/lists/{listName}")
    @POST @Path("/list/{listName}")
    @Consumes("text/plain")
    public void createList(@PathParam("listName") String listName) { 
        
    	System.out.format("Entró a createList en Usuario con: %s%n",listName);
    }    
    
}
