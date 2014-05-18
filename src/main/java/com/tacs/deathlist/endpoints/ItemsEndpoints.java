package com.tacs.deathlist.endpoints;

import com.tacs.deathlist.repository.ListasDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/users/{uid}/lists/{listName}/items/{itemName}")
public class ItemsEndpoints {

    @Autowired
    private ListasDao dao;	
	
	/**
	 * Crea un nuevo ítem en una lista.
	 * 
	 * @param uid
	 * @param listName
	 * @param itemName
	 * @return
	 */
	@POST
	public Response createItem(@PathParam("uid") String uid,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
		dao.createItem(uid, listName, itemName);
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
	public Response voteItem(@PathParam("uid") String uid,
			                 @PathParam("listName") String listName,
			                 @PathParam("itemName") String itemName) {	
	    dao.voteItem(uid, listName, itemName);
		return Response.status(Status.CREATED).build();
	}

	/**
	 * Elimina un item.
	 * 
	 * @param itemName
	 * @return
	 */
	@DELETE
	public Response deleteItem(@PathParam("uid") String uid,
			                   @PathParam("listName") String listName,
			                   @PathParam("itemName") String itemName) {
	    dao.deteleItem(uid, listName, itemName);
		return Response.status(Status.OK).build();
	}

}
