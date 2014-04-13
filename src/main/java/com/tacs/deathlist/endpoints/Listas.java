package com.tacs.deathlist.endpoints;

import java.util.ArrayList;
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

@Path("/users/{username}/list/{listName}")
public class Listas {
    
    private List<String> list = new ArrayList<>();
    private Gson gsonParser = new Gson();
    
    public Listas() {
        list.add("elemento 1");
        list.add("elemento 2");
        list.add("elemento 3");
    }
  
    // Recupera una lista del set de listas de un usuario.
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getList(@PathParam("listName") String listName,
            @PathParam("username") String username) { 
        System.out.format("Entró a getList en Usuario con: %s%n",listName);
        return Response.status(Response.Status.OK).entity(gsonParser.toJson(list)).build();
    }
    
    // Crea una nueva lista.
    @POST 
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createList(@PathParam("listName") String listName) { 
        System.out.format("Entró a createList en Usuario con: %s%n",listName);
        return Response.status(Status.CREATED).build();
    }
    
    // Elimina una lista del set de listas de un usuario.
    @DELETE 
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteList(@PathParam("listName") String listName,
                               @PathParam("username") String username) { 
        System.out.format("Entró a getList en Usuario con " + listName + " y nombre de lista: " + listName);
        return Response.status(Status.OK).build();    
    }
}