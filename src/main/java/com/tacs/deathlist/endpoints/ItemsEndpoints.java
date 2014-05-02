package com.tacs.deathlist.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.tacs.deathlist.repository.InMemoryRepository;
import com.tacs.deathlist.repository.Repository;

@Path("/users/{userName}/lists/{listName}/items/{itemName}")
public class ItemsEndpoints {

    private Repository repository = new InMemoryRepository();	
	
	/**
	 * Crea un nuevo ítem en una lista.
	 * 
	 * @param userName
	 * @param listName
	 * @param itemName
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createItem(@PathParam("userName") String userName,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
		repository.createItem(userName, listName, itemName);
		return Response.status(Status.CREATED).build();
	}

	/**
	 * Votación de un ítem.
	 * 
	 * @param itemName
	 * @return
	 */
	@Path("vote")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response voteItem(@PathParam("userName") String userName,
			                 @PathParam("listName") String listName,
			                 @PathParam("itemName") String itemName) {	
		repository.voteItem(userName, listName, itemName);
		return Response.status(Status.CREATED).build();
	}

	/**
	 * Elimina un item.
	 * 
	 * @param itemName
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteItem(@PathParam("userName") String userName,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
		repository.deteleItem(userName, listName, itemName);
		return Response.status(Status.OK).build();
	}

}
