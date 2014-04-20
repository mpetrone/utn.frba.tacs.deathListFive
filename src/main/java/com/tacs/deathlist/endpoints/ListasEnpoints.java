package com.tacs.deathlist.endpoints;

import java.lang.reflect.Type;
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
import com.google.gson.reflect.TypeToken;
import com.tacs.deathlist.dao.InMemoryListDao;
import com.tacs.deathlist.domain.Lista;

@Path("/users/{username}/lists")
public class ListasEnpoints {
    
    private Gson gsonParser = new Gson();
    private InMemoryListDao dao = new InMemoryListDao();
    
    /**
     * Recupera todas las listas de un usuario. 
     * @param username
     * @return the http response
     */
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLists(@PathParam("username") String username) { 
        List<Lista> listas = dao.getAllLists();
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
        Lista lista = dao.getLista(listName);
        Response response;
        if(lista != null){
            response = Response.status(Response.Status.OK).entity(gsonParser.toJson(lista)).build();
        }else{
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
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
    public Response createList(String jsonBody,
                               @PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        Lista lista = new Lista(listName);
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> items = gsonParser.fromJson(jsonBody, type);
        for (String nombreItem : items) {
            lista.agregarItem(nombreItem);
        }
        dao.createLista(listName, lista);
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
        dao.deleteLista(listName);
        return Response.status(Status.OK).build();    
    }
}

