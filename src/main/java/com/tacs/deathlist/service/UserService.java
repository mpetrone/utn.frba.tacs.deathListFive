package com.tacs.deathlist.service;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

public abstract class UserService {

    public abstract Usuario getUsuarioFromUid(String uid);
    
    abstract Usuario getUsuarioFromToken(String requestorToken);

    public abstract void createUsuario(String requestorToken);

    public abstract void deleteUsuario(String uid);

    public abstract List<Usuario> getFriends(String requestorToken);

    protected abstract boolean esElMismoUsuario(String token, String uid);
    
    protected abstract boolean sonAmigos(String token, String uid);
    
    public abstract void validateToken(String token);
    
	public void validateIdentity(String token, String uid) {

		if (!this.esElMismoUsuario(token, uid))
			throw new CustomForbiddenException("El usuario con token = "
					+ token.toString() + " no es el de uid = " + uid.toString());
	}

	public void validateIdentityOrFriendship(String token, String uid) {
    	
    	if (!this.esElMismoUsuario(token, uid)) {
    		
    		if (!this.sonAmigos(token, uid))
    			throw new CustomForbiddenException("El usuario con token = " + token.toString() + " no es el de uid = " + uid.toString() + " y tampoco es su amigo");
    		
    	}
    }
    
    abstract void enviarNotificacion(String uidReceptor, String mensaje);
    
    public abstract void enviarNotificacionSiCorresponde(String tokenEmisor, String uidReceptor, String itemName, String listName);
	
	public abstract void publicarEnNewsfeed(String uid, String mensaje);
	
	public abstract String getTokenInCookies(HttpHeaders hh);

}
