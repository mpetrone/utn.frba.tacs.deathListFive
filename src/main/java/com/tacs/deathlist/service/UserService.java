package com.tacs.deathlist.service;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserService {

	@Autowired
    private UsuariosDao usuariosDao;
	
	/**
     * Este método obtiene un usuario desde su id.
     * 
     * @param uid el id del usuario     * 
     * @return el usuario con el id especificado
     * 
     */	
	public Usuario getUsuarioFromUid(String uid){
    	
        return usuariosDao.getUsuario(uid);
    }
    
	/**
     * Este método obtiene un usuario desde su token.
     * 
     * @param token el token del usuario
     * @return el usuario con el token especificado
     * 
     */	
	protected Usuario getUsuarioFromToken(String token){
        return this.getUsuarioFromUid(this.getUidFromToken(token));
    }
    
    protected abstract String getUidFromToken(String token);
    
    public abstract void createUsuarioFromToken(String requestorToken);
    
    protected void createUsuario(Usuario usuario) {
    	usuariosDao.createUsuario(usuario);
    }
    
    public void deleteUsuario(String uid) {
        usuariosDao.deleteUsuario(uid);
    }
    
    /**
     * Este método determina si dos representaciones
     * de usuarios corresponden al mismo.
     * 
     * @param token el token de un usuario
     * @param uid el id de otro usuario 
     * @return true si es el mismo, false si son distintos
     * 
     */	
    protected boolean esElMismoUsuario(String token, String uid) {
    	
    	return this.getUidFromToken(token).equals(uid);
    }
    
    /**
     * Este método obtiene una lista con los amigos de un usuario
     * 
     * @param token el token de un usuario
     * 
     */	
    public abstract List<Usuario> getFriends(String token);
        
    /**
     * Este método determina si dos representaciones
     * de usuarios corresponden a usuarios que son amigos.
     * 
     * @param token el token de un usuario
     * @param uid el id de otro usuario 
     * @return true si son amigos, false si no son amigos
     * 
     */	
    protected boolean sonAmigos(String token, String uid) {
        
        /* Esta opcion funciona.
        
    	for (Usuario usuario : this.getFriends(token)) {
        	if (usuario.getUid().equals(uid)) {
        		return true;
        	}
        } 
        
        return false;
        */
    	
    	// TODO: si esta también funciona, dejar esta.
    	return this.getFriends(token).contains(this.getUsuarioFromUid(uid));
	}
    
    /**
     * Este método valida si dos representaciones de usuarios
     * corresponden al mismo usuario
     * 
     * @param token el token de un usuario
     * @param uid el id de otro usuario 
     * @throws CustomForbiddenException si son usuarios distintos
     * 
     */	
    public void validateIdentity(String token, String uid) {

		if (!this.esElMismoUsuario(token, uid))
			throw new CustomForbiddenException("El usuario con token = "
					+ token.toString() + " no es el de uid = " + uid.toString());
	}
	
    /**
     * Este método valida si dos representaciones de usuarios
     * corresponden al mismo usuario o a dos usuarios que son amigos.
     * 
     * @param token el token de un usuario
     * @param uid el id de otro usuario 
     * @throws CustomForbiddenException si son usuarios distintos y no amigos
     * 
     */	
    public void validateIdentityOrFriendship(String token, String uid) {
    	
    	if (!this.esElMismoUsuario(token, uid)) {
    		
    		if (!this.sonAmigos(token, uid))
    			throw new CustomForbiddenException("El usuario con token = " + token.toString() + " no es el de uid = " + uid.toString() + " y tampoco es su amigo");
    		
    	}
    }
    
	protected void enviarNotificacion(String uidReceptor, String mensaje) {
		// por defecto no se hace nada; ver implementaciones.
	}
    
    public void enviarNotificacionSiCorresponde(String tokenEmisor, String uidReceptor, String itemName, String listName) {
    	// por defecto no se hace nada; ver implementaciones.
    }

	public void publicarEnNewsfeed(String uid, String mensaje) {
		// por defecto no se hace nada; ver implementaciones.
	}
	
	public abstract String getTokenInCookies(HttpHeaders hh);
	
    public void validateToken(String token) {
        //por defecto todos son validos
    }

}
