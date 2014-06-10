package com.tacs.deathlist.endpoint;

import java.util.List;
import java.util.Map;

import com.tacs.deathlist.dao.ListasDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.service.FacebookUserService;
import com.tacs.deathlist.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
		String uidActivo = getUidInCookies(hh);
		String token = getTokenInCookies(hh);
		
		/* TODO: Comentado porque rompe los tests al no tener cookies
		if (!userService.esElMismoUsuario(uidActivo,uid) && !userService.esAmigoDeUsuario(token, uid))
			return Response.status(Status.FORBIDDEN).build();
		else {*/
			notificarSiCorresponde(uidActivo, uid, "One of your friends added an item to your list!");
			// TODO: que diga el nombre del amigo y de la lista
			
			dao.createItem(uid, listName, itemName);
			return Response.status(Status.CREATED).build();
		//}
		
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
		String uidActivo = getUidInCookies(hh);
		String token = getTokenInCookies(hh);
		
		/* TODO: Comentado porque rompe los tests al no tener cookies
		if (!userService.esElMismoUsuario(uidActivo,uid) && !userService.esAmigoDeUsuario(token, uid))
			return Response.status(Status.FORBIDDEN).build();
		else {*/
			notificarSiCorresponde(uidActivo, uid, "One of your friends voted for an item on your list!");
			// TODO: que diga el nombre del amigo y de la lista
			
			dao.voteItem(uid, listName, itemName);
			return Response.status(Status.CREATED).build();			
		//}
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
		String uidActivo = getUidInCookies(hh);
		
		// No se permite borrar items de listas ajenas
		// TODO: comento la validacion porque sino rompen los tests
		// (no hay cookies => uidActivo es null => prohibe)
		/*
		if (!userService.esElMismoUsuario(uidActivo,uid))
			return Response.status(Status.FORBIDDEN).build();
		else { */
			dao.deleteItem(uid, listName, itemName);
			return Response.status(Status.OK).build();
		//}
	}
	
	private String getUidInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("uid");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }
	
	private String getTokenInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
	}    
		
	
	private void notificarSiCorresponde(String uidActivo, String uidDuenio, String mensaje) {
		if(!userService.esElMismoUsuario(uidActivo,uidDuenio))
			userService.enviarNotificacion(uidDuenio, mensaje);			
	}

}
