package com.tacs.deathlist.endpoints;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.repository.ListasDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;


@Path("/users/{uid}/lists")
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
    public Response getAllLists(@PathParam("uid") String uid) {
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
							@PathParam("uid") String uid) {
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
                               @PathParam("uid") String uid) {
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
                               @PathParam("uid") String uid) {
        dao.deleteLista(uid, listName);
        return Response.status(Status.OK).build();    
    }
}

