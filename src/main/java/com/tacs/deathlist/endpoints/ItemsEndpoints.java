package com.tacs.deathlist.endpoints;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.tacs.deathlist.repository.ListasDao;

@Path("/users/{userName}/lists/{listName}/items/{itemName}")
public class ItemsEndpoints {

    @Autowired
    private ListasDao dao;	
	
	/**
	 * Crea un nuevo ítem en una lista.
	 * 
	 * @param userName
	 * @param listName
	 * @param itemName
	 * @return
	 */
	@POST
	public Response createItem(@PathParam("userName") String userName,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
		dao.createItem(userName, listName, itemName);
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
	public Response voteItem(@PathParam("userName") String userName,
			                 @PathParam("listName") String listName,
			                 @PathParam("itemName") String itemName) {	
	    dao.voteItem(userName, listName, itemName);
		return Response.status(Status.CREATED).build();
	}

	/**
	 * Elimina un item.
	 * 
	 * @param itemName
	 * @return
	 */
	@DELETE
	public Response deleteItem(@PathParam("userName") String userName,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
	    dao.deteleItem(userName, listName, itemName);
		return Response.status(Status.OK).build();
	}

}
