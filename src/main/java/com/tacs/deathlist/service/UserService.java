package com.tacs.deathlist.service;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserService {

	@Autowired
    protected UsuariosDao usuariosDao;
	
	public Usuario getUsuarioFromUid(String uid){
    	
        return usuariosDao.getUsuario(uid);
    }
    
    abstract Usuario getUsuarioFromToken(String requestorToken);
    
    protected abstract String getUidFromToken(String token);
    
    public abstract void createUsuario(String requestorToken);
    
    public void deleteUsuario(String uid) {
        usuariosDao.deleteUsuario(uid);
    }
    
    protected boolean esElMismoUsuario(String token, String uid) {
    	
    	return this.getUidFromToken(token).equals(uid);
    }
    
    public abstract List<Usuario> getFriends(String requestorToken);
        
    protected boolean sonAmigos(String token, String uid) {
        
        return this.getFriends(token).contains(this.getUsuarioFromUid(uid));
	}
    
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
	
	public abstract void validateToken(String token);

}
