package com.tacs.deathlist.service;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

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

    public Usuario getUser(String token){
        Object element = cacheManager.get(token);

        if(element != null){
            return (Usuario) element;
        }

        FacebookClient facebookClient = new DefaultFacebookClient(token);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        if(facebookUser == null || facebookUser.getId() == null){
            return null;
        }

        Usuario user = new Usuario(facebookUser.getId(), facebookUser.getName());
        cacheManager.put(token, user);
        return user;
    }

    public List<Usuario> getFriends(String token){

        List<Usuario> allFriendsLists = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(token);
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
    
    public boolean esAmigoDeUsuario(String token, String uidFriend) {
		
		List<Usuario> friends = this.getFriends(token);
		
		for (Usuario friend : friends) {
			if (friend.getUid() == uidFriend) {
				return true;
			}   
		}
		
		return false;
	}
    
    public boolean esElMismoUsuario(String uid1, String uid2) {
		// TODO: cambiar null por excepcion
		return uid1 != null && uid1.equalsIgnoreCase(uid2);
	}
    
    public String getTokenInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }
     
	public String getUidInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("uid");
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
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
