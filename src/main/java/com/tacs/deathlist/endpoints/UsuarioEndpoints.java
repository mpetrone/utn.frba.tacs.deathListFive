package com.tacs.deathlist.endpoints;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacs.deathlist.dao.InMemoryUserDao;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.ListasEnpoints;

@Path("/users/{username}")
public class UsuarioEndpoints {

    private Gson gsonParser = new Gson();
    private InMemoryUserDao dao = new InMemoryUserDao();

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
    public Response createUser(String jsonBody, 
    						   @PathParam("username") String username) {
        Usuario user = new Usuario(username);
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> listas = gsonParser.fromJson(jsonBody, type);
        for (String nombreLista : listas) {
            user.agregarLista(nombreLista);
        }
        dao.createUsuario(username, user);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Modifica un Usuario existente (solo username).
     * @param username
     * @return the http response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifyUser(String jsonBody,
    						   @PathParam("username") String username) {
    	Usuario user;
    	Type type = new TypeToken<String>(){}.getType();
    	String nuevoUsername = gsonParser.fromJson(jsonBody, type);
    	user = dao.getUsuario(username);
    	user.modifyUsername(nuevoUsername);
    	dao.modifyUsuario(username, user);
    	// TODO: aca queda mapeado el nombre viejo del usuario (username) y en el objeto (user)
    	// tiene el nuevo nombre de usuario (nuevoUsername), no habría que eliminar la
    	// entrada vieja del map y agregar una nueva con la key nuevoUsername?
        return Response.status(Response.Status.OK).build();
    }

}