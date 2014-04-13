package com.tacs.deathlist.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("item/{itemName}")
public class Items {
	
	// Crea un nuevo ítem en una lista.
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(@PathParam("itemName") String itemName) { 
        System.out.format("Entró a createItem en Item con: %s%n",itemName);
        return Response.status(Status.CREATED).build();
    }
	
	// Votación de un ítem.
	@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response voteItem(@PathParam("itemName") String itemName) { 
        System.out.format("Entró a voteItem en Item con: %s%n",itemName);
        return Response.status(Status.CREATED).build();
    } 
	
	// Elimina un item.
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteItem(@PathParam("itemName") String itemName) {
		System.out.format("Entró a deleteItem en Item con: %s%n", itemName);
		return Response.status(Status.OK).build();
	}

}
