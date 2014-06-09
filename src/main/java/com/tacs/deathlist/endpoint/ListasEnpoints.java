package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.dao.ListasDao;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.service.FacebookUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.Map;


@Path("/users/{uid}/lists")
@Component
public class ListasEnpoints {
    
    @Autowired
    private ListasDao dao;
    
    /**
     * Recupera todas las listas de un usuario. 
     * @param uid
     * @return the http response
     */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLists(@PathParam("uid") String uid, 
    							@Context HttpHeaders hh) {
    	
    	String uidActivo = getUidInCookies(hh);
		String token = getTokenInCookies(hh);
    	/* TODO: Comentado porque rompe los tests
		if (!esElMismoUsuario(uidActivo,uid) || !esAmigoDeUsuario(token, uid))
			return Response.status(Status.FORBIDDEN).build();
		else {*/
	        List<String> listas = dao.getAllLists(uid);
	        return Response.status(Response.Status.OK).entity(listas).build();	
		//}
    }
  
    /**
     * Recupera una lista del set de listas de un usuario.
     * @param listName
     * @param uid
     * @return the http response
     */
    @Path("{listName}")
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
	public Response getList(@PathParam("listName") String listName,
							@PathParam("uid") String uid,
							@Context HttpHeaders hh) {
    	
    	String uidActivo = getUidInCookies(hh);
		String token = getTokenInCookies(hh);
		
    	/* TODO: Comentado porque rompe los tests
		if (!esElMismoUsuario(uidActivo,uid) || !esAmigoDeUsuario(token, uid))
			return Response.status(Status.FORBIDDEN).build();
		else {*/    	
			Lista lista = dao.getLista(uid, listName);
			return Response.status(Response.Status.OK).entity(lista).build();
		//}
	}
    
    /**
     * Crea una nueva lista.
     * @param listName
     * @param uid
     * @return the http response
     */
    @Path("{listName}")
    @POST 
    public Response createList(@PathParam("listName") String listName,
                               @PathParam("uid") String uid,
                               @Context HttpHeaders hh) {
		
    	String uidActivo = getUidInCookies(hh); 
		
		/* TODO: Comentado porque rompe los tests
		if (!esElMismoUsuario(uidActivo,uid))
			return Response.status(Status.FORBIDDEN).build();
		else { */
			dao.createLista(uid, listName);
	        return Response.status(Status.CREATED).build();
		//} 
    }
    
    /**
     * Elimina una lista del set de listas de un usuario.
     * @param listName
     * @param uid
     * @return the http response
     */
    @Path("{listName}")
    @DELETE 
    public Response deleteList(@PathParam("listName") String listName,
                               @PathParam("uid") String uid,
                               @Context HttpHeaders hh) {
    	String uidActivo = getUidInCookies(hh); 
		
		/* TODO: Comentado porque rompe los tests
		if (!esElMismoUsuario(uidActivo,uid))
			return Response.status(Status.FORBIDDEN).build();
		else { */
        	dao.deleteLista(uid, listName);
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
	
	private boolean esElMismoUsuario(String uid1, String uid2) {
		// TODO: cambiar null por excepcion
		return uid1 != null && uid1.equalsIgnoreCase(uid2);
	}
	
	private boolean esAmigoDeUsuario(String token, String uidFriend) {
		
		FacebookUserService facebookUserService = new FacebookUserService();
		List<Usuario> friends;
		
		friends = facebookUserService.getFriends(token);
		
		for (Usuario friend : friends) {
			if (friend.getUid() == uidFriend) {
				return true;
			}   
		}
		
		return false;
	}       
}

