package com.tacs.deathlist.service;

import com.tacs.deathlist.domain.Usuario;

import org.apache.commons.lang3.StringUtils;

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
    
	@Override
    protected String getUidFromToken(String token) {
    	return StringUtils.right(token, 1);
    }
    
	@Override
	public void createUsuarioFromToken(String requestorToken) {
		this.createUsuario(new Usuario(this.getUidFromToken(requestorToken), requestorToken));
	}

    @Override
    public List<Usuario> getFriends(String token) {
        
    	return new ArrayList<Usuario>(Arrays.asList(new Usuario[] {
    			new Usuario("3", "user3"),
    			new Usuario("5", "user5"),
    			new Usuario("7", "user7"),
    			new Usuario("9", "user9"),
    		}));
    	
    }
	
	@Override
	public String getTokenInCookies(HttpHeaders hh){
        
		// el usuario activo en todos los tests es el user1
		// es amigo de los users 3, 5, 7 y 9.
		return "user1";
    }

}
