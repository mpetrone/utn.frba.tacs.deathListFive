package com.tacs.deathlist.endpoint;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tacs.deathlist.dao.ListasDao;
import com.tacs.deathlist.service.UserService;

@Path("/users/{uid}/lists/{listName}/items/{itemName}")
@Component
public class ItemsEndpoints {

    @Autowired
    private ListasDao dao;
    
    @Autowired
    private UserService userService;
	
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
			                   @PathParam("itemName") String itemName,
			                   @Context HttpHeaders hh) {
        String requestorToken = userService.getTokenInCookies(hh);
         
        // si la validación falla se lanza excepción
        userService.validateIdentityOrFriendship(requestorToken, uid);
        
        dao.createItem(uid, listName, itemName);
        
        userService.enviarNotificacionSiCorresponde(requestorToken, uid, itemName, listName);
        
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
			                 @PathParam("itemName") String itemName,
			                 @Context HttpHeaders hh) {
        String requestorToken = userService.getTokenInCookies(hh);
        
        userService.validateIdentityOrFriendship(requestorToken, uid);

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
			                   @PathParam("itemName") String itemName,
			                   @Context HttpHeaders hh) {
        String requestorToken = userService.getTokenInCookies(hh);
        
        userService.validateIdentity(requestorToken, uid);      

        dao.deleteItem(uid, listName, itemName);

        return Response.status(Status.OK).build();
	}


}
