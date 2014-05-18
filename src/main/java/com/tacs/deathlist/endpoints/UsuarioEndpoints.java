package com.tacs.deathlist.endpoints;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.repository.UsuariosDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{uid}")
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao dao;

    /**
     * Recupera un Usuario.
     * @param uid
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("uid") String uid) {
		Usuario user = dao.getUsuario(uid);
		return Response.status(Response.Status.OK).entity(user).build();
	}

    /**
     * Crea un nuevo Usuario.
     * @param uid
     * @return the http response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("uid") String uid,
            UserCreationRequest request) {
        Usuario user = new Usuario(uid, request.getNombre());
        dao.createUsuario(uid, user);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Elimina un usuario.
     * @param uid
     * @return the http response
     */
    @DELETE
    public Response deleteUser(@PathParam("uid") String uid) {
        dao.deleteUsuario(uid);
        return Response.status(Response.Status.OK).build();
    }
}