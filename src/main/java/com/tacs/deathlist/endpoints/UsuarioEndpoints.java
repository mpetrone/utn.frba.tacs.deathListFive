package com.tacs.deathlist.endpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.repository.ListasDao;
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
    	
    	Map<String, List<String>> allFriendsLists; 
    	
    	allFriendsLists = this.obtenerListasDeAmigosDeFacebook(MY_ACCESS_TOKEN);
    	
		return Response.status(Response.Status.OK).entity(allFriendsLists).build();
	}
    
    public Map<String, List<String>> obtenerListasDeAmigosDeFacebook(String accessToken) {
    	
    	Map<String, List<String>> allFriendsLists = new HashMap<String, List<String>>();
    	
    	FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
    	Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class); 
		
    	//TODO: ESTA BIEN ACCEDER AL DAO DE LISTAS DESDE ACA?
    	ListasDao listasDao;
		
		for (User friend : myFriends.getData()) {
			String friendId = friend.getId();
			//String friendName = friend.getName();
			
			if (dao.getUsuario(friendId) != null) {
				/* TODO: ERROR: NO PUEDO ACCEDER AL DAO DE LISTAS DESDE ACA
				List<String> friendLists = listasDao.getAllLists(friendId);
				
				allFriendsLists.put(friendId, friendLists); 	
				*/	
			} 
		}
    	
    	return allFriendsLists;
    }
}