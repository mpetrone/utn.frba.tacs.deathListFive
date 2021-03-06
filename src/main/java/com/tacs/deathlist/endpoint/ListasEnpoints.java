package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.dao.ListasDao;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;


@Path("/users/{uid}/lists")
@Component
public class ListasEnpoints {
    
    @Autowired
    private ListasDao dao;
    
    @Autowired
    private UserService userService;
    
    /**
     * Recupera todas las listas de un usuario. 
     * @param uid
     * @return the http response
     */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLists(@PathParam("uid") String uid, 
    							@Context HttpHeaders hh) {

        String requestorToken = userService.getTokenInCookies(hh);

        userService.validateIdentityOrFriendship(requestorToken, uid);

        List<String> listas = dao.getAllLists(uid);
        return Response.status(Response.Status.OK).entity(listas).build();
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

        String requestorToken = userService.getTokenInCookies(hh);

        userService.validateIdentityOrFriendship(requestorToken, uid);

        Lista lista = dao.getLista(uid, listName);

        return Response.status(Response.Status.OK).entity(lista).build();
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

        String requestorToken = userService.getTokenInCookies(hh);

        userService.validateIdentity(requestorToken, uid);

        dao.createLista(uid, listName);
        
        return Response.status(Status.CREATED).build();
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
        String requestorToken = userService.getTokenInCookies(hh);

        userService.validateIdentity(requestorToken, uid);

        dao.deleteLista(uid, listName);
        
        return Response.status(Status.OK).build();
    }
	       
}

