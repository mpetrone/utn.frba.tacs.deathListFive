package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import java.util.List;

@Path("/users/{uid}")
@Component
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao usuariosDao;

    @Autowired
    private UserService userService;

    /**
     * Recupera un Usuario.
     * @param uid
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("uid") String uid) {
		Usuario usuario = usuariosDao.getUsuario(uid);
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
        String token = userService.getTokenInCookies(hh);
        Usuario user = userService.getUser(token);

        if(!user.getUid().equalsIgnoreCase(uid)){
            throw new CustomNotFoundException("El uid usuario " + user.getUid()
                    + "es distinto al uid que fue mandado por uri " + uid);
        }

        usuariosDao.createUsuario(user);
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
        String uidActivo = userService.getUidInCookies(hh); 
    	
        /* TODO: Comentado porque rompe los tests */
    	if (!userService.esElMismoUsuario(uidActivo,uid))
    		return Response.status(Status.FORBIDDEN).build();
    	else {
    		usuariosDao.deleteUsuario(uid);
    		return Response.status(Response.Status.OK).build();
    	}
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
        String uidActivo = userService.getUidInCookies(hh); 
    	
        /* TODO: Comentado porque rompe los tests */
    	if (!userService.esElMismoUsuario(uidActivo,uid))
    		return Response.status(Status.FORBIDDEN).build();
    	else {
            Usuario usuario = usuariosDao.getUsuario(uid);
            if (usuario == null){
                throw new CustomNotFoundException("El usuario " + uid
                        + " no existe en el sistema.");
            }

            String token = userService.getTokenInCookies(hh);

        	List<Usuario> friendsList = userService.getFriends(token);
        	
    		return Response.status(Response.Status.OK).entity(friendsList).build();
    	}
	}
	 	
}
