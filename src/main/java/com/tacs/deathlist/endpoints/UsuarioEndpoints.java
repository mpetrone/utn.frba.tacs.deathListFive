package com.tacs.deathlist.endpoints;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.tacs.deathlist.dao.InMemoryRepository;
import com.tacs.deathlist.dao.Repository;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

@Path("/users/{username}")
public class UsuarioEndpoints {

    private Gson gsonParser = new Gson();
    private Repository dao = new InMemoryRepository();  

    /**
     * Recupera un Usuario.
     * @param username
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        Usuario user = dao.getUsuario(username);
        Response response;
        if (user != null){
            response = Response.status(Response.Status.OK).entity(gsonParser.toJson(user)).build();
        }
        else{
            response = Response.status(Response.Status.NOT_FOUND).build();
        }
        return response;
    }

    /**
     * Crea un nuevo Usuario.
     * @param username
     * @return the http response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("username") String username,
            String jsonRequest) {
        UserCreationRequest request = gsonParser.fromJson(jsonRequest, UserCreationRequest.class);
        Usuario user = new Usuario(username, request.getUid(), request.getToken());
        dao.createUsuario(username, user);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Elimina un usuario.
     * @param username
     * @return the http response
     */
    @DELETE
    public Response deleteUser(@PathParam("username") String username) {
        dao.deleteUsuario(username);
        return Response.status(Response.Status.OK).build();
    }
}