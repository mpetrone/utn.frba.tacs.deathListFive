package com.tacs.deathlist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomInternalServerErrorException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;


/**
 *
 * Servicio que provee la informacion de los usuarios de Facebook.
 *
 */
@Component
public class FacebookUserService extends UserService {

    public static final String CACHE_NAME = "facebookUsersCache";

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
    protected String getUidFromToken(String token) {
    	
    	return this.getFacebookUser(token).getId();
    }
    
    @Override
    public void createUsuarioFromToken(String requestorToken) {
        User requestorFacebookUser = getFacebookUser(requestorToken);
        Usuario usuario = new Usuario(requestorFacebookUser.getId(), requestorFacebookUser.getName());
        this.createUsuario(usuario);
    }
    
    /**
     * Este método obtiene un usuario de facebook como instancia de
     * la clase User del framework restfb. Se utiliza un cache
     * de GAE para evitar requests innecesarios a facebook.
     * 
     * @param token el token de un usuario
     * @throws CustomNotFoundException si no se pudo obtener el usuario
     * 
     */	
    private User getFacebookUser(String token){
        Object element = cacheManager.get(token);

        if(element != null){
            return (User) element;
        }

        FacebookClient facebookClient = new DefaultFacebookClient(token,this.appSecret);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        if(facebookUser == null || facebookUser.getId() == null){
        	throw new CustomNotFoundException("No se pudo obtener el usuario de facebook con token = " + token.toString());
        }

        cacheManager.put(token, facebookUser);
        return facebookUser;
    }

    @Override
    public List<Usuario> getFriends(String token){
        
    	List<Usuario> listaDeAmigos = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(token,this.appSecret);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);

        if(myFriends != null && myFriends.getData() != null) {
            for (User friend : myFriends.getData()) {
                String friendId = friend.getId();
                
                try {
                	Usuario userFriend = this.getUsuarioFromUid(friendId);
                    listaDeAmigos.add(userFriend);
                }
                catch (CustomNotFoundException e) {
                	// ese amigo no usa nuestra app, lo descartamos
                }
            }
        }

        return listaDeAmigos;
    }

    @Override
    protected void enviarNotificacion(String uidReceptor, String mensaje) {
	    
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
    public void enviarNotificacionSiCorresponde(String tokenEmisor, String uidReceptor, String itemName, String listName) {
    	
    	Usuario usuario = this.getUsuarioFromToken(tokenEmisor);

		if(!this.esElMismoUsuario(tokenEmisor, uidReceptor))
            this.enviarNotificacion(uidReceptor,
                    usuario.getNombre() + " ha creado el item " + itemName + " en la lista " + listName);
        
    }
	
	@Override
	public String getTokenInCookies(HttpHeaders hh){
        Map<String, Cookie> pathParams = hh.getCookies();
        Cookie cookie = pathParams.get("token");
        
        if(cookie == null){
            throw new CustomInternalServerErrorException("Se produjo un error al obtener la cookie de los headers");
        }
        return cookie.getValue();
    }
	
	 @Override
	 public void validateToken(String token) {
		 User user = getFacebookUser(token);
		 if(user == null)
			 throw new CustomForbiddenException("el token es invalido");
		 
	}
}
