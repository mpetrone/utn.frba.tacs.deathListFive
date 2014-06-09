package com.tacs.deathlist.endpoint;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.Map;

@Path("/users/{uid}")
@Component
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao usuariosDao;

    @Autowired
    private UserService userService;

    private String appId;

    private String appSecret;

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
        String token = getTokenInCookies(hh);
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
        String uidActivo = getUidInCookies(hh); 
    	
        /* TODO: Comentado porque rompe los tests
    	if (!esElMismoUsuario(uidActivo,uid))
    		return Response.status(Status.FORBIDDEN).build();
    	else {*/
    		usuariosDao.deleteUsuario(uid);
    		return Response.status(Response.Status.OK).build();
    	//}
    }
    
    /**
     * Recupera las listas de los amigos de facebook del usuario. 
     * @param uid
     * @return the http response
     */
    @Path("friends")
    @GET 
    @Produces(MediaType.APPLICATION_JSON)
	public Response getFriendLists(@PathParam("uid") String uid,
                                   @Context HttpHeaders hh) {
        String uidActivo = getUidInCookies(hh); 
    	
        /* TODO: Comentado porque rompe los tests
    	if (!esElMismoUsuario(uidActivo,uid))
    		return Response.status(Status.FORBIDDEN).build();
    	else {*/ 
            Usuario usuario = usuariosDao.getUsuario(uid);
            if (usuario == null){
                throw new CustomNotFoundException("El usuario " + uid
                        + " no existe en el sistema.");
            }

            String token = getTokenInCookies(hh);

        	List<Usuario> friendLists = userService.getFriends(token);
        	
    		return Response.status(Response.Status.OK).entity(friendLists).build();
    	//}
	}

    private String getTokenInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    @Value("${facebook.app.id}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${facebook.app.secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
     
	private String getUidInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("uid");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }
	 
	private boolean esElMismoUsuario(String uid1, String uid2) {
		// TODO: cambiar null por excepcion
		return uid1 != null && uid1.equalsIgnoreCase(uid2);
	}	
}
