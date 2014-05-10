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

import org.springframework.beans.factory.annotation.Autowired;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.repository.UsuariosDao;

@Path("/users/{username}")
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao dao;

    /**
     * Recupera un Usuario.
     * @param username
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("username") String username) {
		Usuario user = dao.getUsuario(username);
		return Response.status(Response.Status.OK).entity(user).build();
	}

    /**
     * Crea un nuevo Usuario.
     * @param username
     * @return the http response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("username") String username,
            UserCreationRequest request) {
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