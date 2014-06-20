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
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomInternalServerErrorException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;

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
    public Usuario getUsuarioFromToken(String token){

        return this.getUsuarioFromUid(getFacebookUser(token).getId());
    }
    
    @Override
    protected String getUidFromToken(String token) {
    	
    	return this.getFacebookUser(token).getId();
    }
    
    @Override
    public void createUsuario(String requestorToken) {
        User requestorFacebookUser = getFacebookUser(requestorToken);
        Usuario usuario = new Usuario(requestorFacebookUser.getId(), requestorFacebookUser.getName());
        usuariosDao.createUsuario(usuario);
    }
    
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
    public List<Usuario> getFriends(String requestorToken){
        
    	List<Usuario> listaDeAmigos = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(requestorToken,this.appSecret);
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
    public void enviarNotificacionSiCorresponde(String tokenEmisor, String uidReceptor, String itemName, String listName) {
    	
    	Usuario usuario = this.getUsuarioFromToken(tokenEmisor);

		if(!this.esElMismoUsuario(tokenEmisor, uidReceptor))
            this.enviarNotificacion(uidReceptor,
                    usuario.getNombre() + " ha creado el item " + itemName + " en la lista " + listName);
        
    }
	
	@Override
	public void publicarEnNewsfeed(String requestorToken, String mensaje) {
		
		FacebookClient facebookClient = new DefaultFacebookClient(requestorToken,this.appSecret);
		
		facebookClient.publish("me/feed", FacebookType.class, Parameter.with("message", mensaje));
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
