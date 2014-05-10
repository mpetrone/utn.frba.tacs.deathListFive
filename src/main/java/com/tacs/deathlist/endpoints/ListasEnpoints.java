package com.tacs.deathlist.endpoints;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.repository.ListasDao;


@Path("/users/{username}/lists")
public class ListasEnpoints {
    
    @Autowired
    private ListasDao dao;
    
    /**
     * Recupera todas las listas de un usuario. 
     * @param username
     * @return the http response
     */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLists(@PathParam("username") String username) { 
        List<String> listas = dao.getAllLists(username);
        return Response.status(Response.Status.OK).entity(listas).build();
    }
  
    /**
     * Recupera una lista del set de listas de un usuario.
     * @param listName
     * @param username
     * @return the http response
     */
    @Path("{listName}")
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
	public Response getList(@PathParam("listName") String listName,
							@PathParam("username") String username) {
		Lista lista = dao.getLista(username, listName);
		return Response.status(Response.Status.OK).entity(lista).build();
	}
    
    /**
     * Crea una nueva lista.
     * @param listName
     * @param username
     * @return the http response
     */
    @Path("{listName}")
    @POST 
    public Response createList(@PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        dao.createLista(username, listName);
        return Response.status(Status.CREATED).build();
    }
    
    /**
     * Elimina una lista del set de listas de un usuario.
     * @param listName
     * @param username
     * @return the http response
     */
    @Path("{listName}")
    @DELETE 
    public Response deleteList(@PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        dao.deleteLista(username, listName);
        return Response.status(Status.OK).build();    
    }
}

