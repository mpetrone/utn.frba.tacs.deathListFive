package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.UserService;
import com.tacs.deathlist.util.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{uid}")
@Component
public class UsuarioEndpoints {

    @Autowired
    private UserService userService;

    /**
     * Recupera un Usuario.
     * @param uid
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("uid") String uid,
                            @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);

		Usuario usuario = userService.getUsuario(requestorToken, uid);
        if (usuario == null){
            throw new CustomNotFoundException("El usuario " + uid
                    + " no existe en el sistema.");
        }
		return Response.status(Response.Status.OK).entity(usuario).build();
	}

    /**
     * Crea un nuevo Usuario.
     * @param uid
     * @return the http response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("uid") String uid,
                               @Context HttpHeaders hh)  {
        String requestorToken = RequestUtils.getTokenInCookies(hh);

        userService.createUsuario(requestorToken, uid);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Elimina un usuario.
     * @param uid
     * @return the http response
     */
    @DELETE
    public Response deleteUser(@PathParam("uid") String uid,
    						   @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);

        userService.deleteUsuario(requestorToken, uid);
        return Response.status(Response.Status.OK).build();

    }
    
    /**
     * Recupera las listas de los amigos de facebook del usuario. 
     * @param uid
     * @return the http response
     */
    @Path("friends")
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
	public Response getFriendsList(@PathParam("uid") String uid,
                                   @Context HttpHeaders hh) {
        String requestorToken = RequestUtils.getTokenInCookies(hh);
    	
        List<Usuario> friendsList = userService.getFriends(requestorToken, uid);
        	
    	return Response.status(Response.Status.OK).entity(friendsList).build();
	}
	 	
}
