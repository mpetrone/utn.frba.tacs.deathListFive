package com.tacs.deathlist.endpoints;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.repository.UsuariosDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/users/{uid}")
public class UsuarioEndpoints {

    @Autowired
    private UsuariosDao usuariosDao;

    /**
     * Recupera un Usuario.
     * @param uid
     * @return the http response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("uid") String uid) {
		Usuario user = usuariosDao.getUsuario(uid);
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
	public Response getFriendLists(@PathParam("uid") String uid) {
		
    	//TODO: ACCESS TOKEN HARCODEADO, tendria que recibirlo por parametro (cookie me imagino)
    	String MY_ACCESS_TOKEN = "CAACEdEose0cBAG9nvNDXgSQSx3eqcPleUuc0KoVGZBGXqxAsJGNqPHylNM7W1pqQdfLt0kZCqZAKrFLeTgWVvXw8NjODjH5z8YsAnjnvoF951MFExfD4rbABmqxUZCiMVDBmL2boU1Bj1L8hcQ3CAyY8d7bmo3qofOZC2cuuwAdDV6KsQKFbRNhZBBqi6RWqyVhUASVsBZC2AZDZD";
    	
    	List<Usuario> friendLists = obtenerListasDeAmigosDeFacebook(MY_ACCESS_TOKEN);
    	
		return Response.status(Response.Status.OK).entity(friendLists).build();
	}
    
    public List<Usuario> obtenerListasDeAmigosDeFacebook(String accessToken) {

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
}
