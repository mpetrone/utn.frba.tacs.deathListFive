package com.tacs.deathlist.service;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Servicio que provee la informacion de los usuarios de Facebook.
 *
 */
@Component
public class FacebookUserService implements UserService {

    public static final String CACHE_NAME = "facebookUsersCache";

    @Autowired
    private UsuariosDao usuariosDao;

    private MemcacheService cacheManager = MemcacheServiceFactory.getMemcacheService();
    
    private String appId;
    private String appSecret;
    
    @Value("${facebook.app.id}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${facebook.app.secret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public Usuario getUsuario(String requestorToken, String uid){
        User requestorFacebookUser = getFacebookUser(requestorToken);

        if(!requestorFacebookUser.getId().equalsIgnoreCase(uid) && !esAmigoDeUsuario(requestorToken, uid)){
            throw new CustomForbiddenException("el usuario solicitante no es amigo del usuario requerido");
        }
        return usuariosDao.getUsuario(uid);
    }

    @Override
    public void createUsuario(String requestorToken, String uid) {
        User requestorFacebookUser = getFacebookUser(requestorToken);
        Usuario usuario = new Usuario(requestorFacebookUser.getId(), requestorFacebookUser.getName());
        usuariosDao.createUsuario(usuario);
    }

    @Override
    public void deleteUsuario(String requestorToken, String uid) {
        User requestorFacebookUser = getFacebookUser(requestorToken);
        if(!requestorFacebookUser.getId().equalsIgnoreCase(uid)){
            throw new CustomForbiddenException("el usuario solicitante no puede eliminar a otro usuario");
        }
        usuariosDao.deleteUsuario(uid);
    }

    @Override
    public void validateToken(String token) {
        User user = getFacebookUser(token);
        if(user == null){
            throw new CustomForbiddenException("el token es invalido");
        }
    }


    private User getFacebookUser(String token){
        Object element = cacheManager.get(token);

        if(element != null){
            return (User) element;
        }

        FacebookClient facebookClient = new DefaultFacebookClient(token);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        if(facebookUser == null || facebookUser.getId() == null){
            return null;
        }

        cacheManager.put(token, facebookUser);
        return facebookUser;
    }

    @Override
    public List<Usuario> getFriends(String requestorToken, String uid){
        User requestorFacebookUser = getFacebookUser(requestorToken);

        if(!requestorFacebookUser.getId().equalsIgnoreCase(uid)){
            throw new CustomForbiddenException("el solicitante no puede ver los amigos de otros usuarios");
        }

        List<Usuario> allFriendsLists = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(requestorToken);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);

        if(myFriends != null && myFriends.getData() != null) {
            for (User friend : myFriends.getData()) {
                String friendId = friend.getId();
                Usuario userFriend = usuariosDao.getUsuario(friendId);
                if (userFriend != null) {
                    allFriendsLists.add(userFriend);
                }
            }
        }

        return allFriendsLists;
    }

    private boolean esAmigoDeUsuario(String requestorToken, String uidFriend) {

        FacebookClient facebookClient = new DefaultFacebookClient(requestorToken);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);

        if(myFriends != null && myFriends.getData() != null) {
            for (User friend : myFriends.getData()) {
                if(friend.getId().equalsIgnoreCase(uidFriend)){
                    return true;
                }
            }
        }

        return false;
	}

    @Override
    public void enviarNotificacion(String uidReceptor, String mensaje) {
	    
	    AccessToken appAccessToken = new DefaultFacebookClient().obtainAppAccessToken(this.appId, this.appSecret);	    
	    FacebookClient facebookClient = new DefaultFacebookClient(appAccessToken.getAccessToken());
	    
	    try {	        
	        facebookClient.publish(uidReceptor + "/notifications", FacebookType.class, Parameter.with("template", mensaje));
	    }	    
	    catch (FacebookOAuthException e) {
	        
	        if (e.getErrorCode() == 200) {
	            // El usuario receptor no usa Deathlist
	        } else if (e.getErrorCode() == 100) {
	            // El mensaje supera los 180 caracteres
	        }
	    }
	}
	
	@Override
	public void publicarEnNewsfeed(String uid, String mensaje) {
		
		// TODO: Decidir si esto se hace en el frontend (botón compartir)
	}

}
