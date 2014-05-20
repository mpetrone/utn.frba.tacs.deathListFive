package com.tacs.deathlist.endpoints;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.repository.UsuariosDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/users/{uid}")
@Component
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao usuariosDao;

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
            UserCreationRequest request) {
        Usuario user = new Usuario(uid, request.getNombre());
        usuariosDao.createUsuario(uid, user);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Elimina un usuario.
     * @param uid
     * @return the http response
     */
    @DELETE
    public Response deleteUser(@PathParam("uid") String uid) {
        usuariosDao.deleteUsuario(uid);
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
	public Response getFriendLists(@PathParam("uid") String uid,
                                   @Context HttpHeaders hh) {

        Usuario usuario = usuariosDao.getUsuario(uid);
        if (usuario == null){
            throw new CustomNotFoundException("El usuario " + uid
                    + " no existe en el sistema.");
        }

        //el filter deberia fijarse de que en las cookies exista un token, sino deberia dar 403
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        cookie.getValue();

    	List<Usuario> friendLists = obtenerListasDeAmigosDeFacebook(cookie.getValue());
    	
		return Response.status(Response.Status.OK).entity(friendLists).build();
	}
    
    private List<Usuario> obtenerListasDeAmigosDeFacebook(String accessToken) {

        List<Usuario> allFriendsLists = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
    	Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);

        if(myFriends != null && myFriends.getData() != null) for (User friend : myFriends.getData()) {
            String friendId = friend.getId();
            Usuario userFriend = usuariosDao.getUsuario(friendId);
            if (userFriend != null) {
            	allFriendsLists.add(userFriend);	
            } 
        }
    	
    	return allFriendsLists;
    }

    @Value("${facebook.app.id}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${facebook.app.secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
