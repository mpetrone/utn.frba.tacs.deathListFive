package com.tacs.deathlist.endpoints;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.tacs.deathlist.dao.InMemoryRepository;
import com.tacs.deathlist.dao.Repository;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.ListaInexistenteException;

@Path("/users/{username}/lists")
public class ListasEnpoints {
    
    private Gson gsonParser = new Gson();
    private Repository repository = new InMemoryRepository();  
    
    /**
     * Recupera todas las listas de un usuario. 
     * @param username
     * @return the http response
     */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLists(@PathParam("username") String username) { 
        List<Lista> listas = repository.getAllLists(username);
        return Response.status(Response.Status.OK).entity(gsonParser.toJson(listas)).build();
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
        Lista lista;
        Response response;
		try {
			lista = repository.getLista(username, listName);
			response = Response.status(Response.Status.OK)
					.entity(gsonParser.toJson(lista)).build();
			return response;
		} catch (ListaInexistenteException lie) {
			response = Response.status(Response.Status.NOT_FOUND).build();
			return response;
		}
    }
    
    /**
     * Crea una nueva lista.
     * @param listName
     * @param username
     * @return the http response
     */
    @Path("{listName}")
    @POST 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createList(@PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        repository.createLista(username, listName);
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteList(@PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        repository.deleteLista(username, listName);
        return Response.status(Status.OK).build();    
    }
}

