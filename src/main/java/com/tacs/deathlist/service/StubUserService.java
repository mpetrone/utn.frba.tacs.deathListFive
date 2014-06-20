package com.tacs.deathlist.service;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

/**
 *
 * Version stub de la obtencion de users.
 *
 */
public class StubUserService extends UserService{

    @Autowired
    private UsuariosDao usuariosDao;
    
    private final List<Usuario> usuariosDePrueba = new ArrayList<Usuario>(Arrays.asList(new Usuario[] {
        new Usuario("1", "user1"),
        new Usuario("2", "user2"),
        new Usuario("3", "user3"),
        new Usuario("4", "user4"),
        new Usuario("5", "user5"),
        new Usuario("6", "user6"),
        new Usuario("7", "user7"),
        new Usuario("8", "user8"),
        new Usuario("9", "user9"),
    }));
    
    private List<Usuario> getUsuariosPares() {
    	
    	List<Usuario> usuariosPares = new ArrayList<Usuario>();
    	
    	for (Usuario usuario : usuariosDePrueba) {
    		if(this.esPar(usuario))
    			usuariosPares.add(usuario);
    	}
    	
    	return usuariosPares;
    }
    
    private List<Usuario> getUsuariosImpares() {
    	List<Usuario> usuariosImpares = this.usuariosDePrueba;
    	
    	usuariosImpares.removeAll(this.getUsuariosPares());
    	
    	return usuariosImpares;
    }
    
    private boolean esPar(Usuario usuario) {
    	return Integer.parseInt(usuario.getUid()) % 2 == 0;
    }
    
    private String getUidFromTokenFicticio(String token) {
    	return StringUtils.right(token, 1);
    }
    

    @Override
    public List<Usuario> getFriends(String token) {
        
    	List<Usuario> friends = new ArrayList<>();
        
    	int numUsuario = Integer.parseInt(this.getUidFromTokenFicticio(token));
    	
    	if (numUsuario % 2 == 0)
    		friends = this.getUsuariosPares();
    	else
    		friends = this.getUsuariosImpares();
    	
    	friends.remove(numUsuario);
    	
        return friends;
    }
    
    @Override
	protected boolean sonAmigos(String token, String uid) {
		
    	for (Usuario usuario : this.getFriends(token)) {
    		if (usuario.getUid().equals(uid))
    			return true;    		
    	}    	
    	
		return false;
	}

    @Override
    public Usuario getUsuarioFromUid(String uid) {
        return usuariosDao.getUsuario(uid);
    }
    
    @Override
    public Usuario getUsuarioFromToken(String token) { 
        return this.getUsuarioFromUid(this.getUidFromTokenFicticio(token));
    }

    @Override
    public void createUsuario(String requestorToken) {
        usuariosDao.createUsuario(new Usuario(this.getUidFromTokenFicticio(requestorToken), requestorToken));
    }

    @Override
    public void deleteUsuario(String uid) {
        usuariosDao.deleteUsuario(uid);
    }

    @Override
    public void validateToken(String token) {
        //todos son validos
    }

    @Override
	public void enviarNotificacion(String uidReceptor, String mensaje) {

	}
    
    public void enviarNotificacionSiCorresponde(String tokenEmisor, String uidReceptor, String itemName, String listName) {
    	
    }

	@Override
	public void publicarEnNewsfeed(String uid, String mensaje) {

	}

	@Override
	protected boolean esElMismoUsuario(String token, String uid) {
		
		return this.getUidFromTokenFicticio(token).equals(uid);
	}
	
	@Override
	public String getTokenInCookies(HttpHeaders hh){
        
		// el usuario activo en todos los tests es el user1
		// es amigo de los users 3, 5, 7 y 9.
		return "user1";
    }

}
