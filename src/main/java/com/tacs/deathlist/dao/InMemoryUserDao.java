package com.tacs.deathlist.dao;

import java.util.HashMap;
import java.util.Map;

import com.tacs.deathlist.domain.Usuario;

public class InMemoryUserDao implements UserDao {
	
	static private Map<String,Usuario> usuarios  = new HashMap<>();
	
	@Override
	public Usuario getUsuario(String username){
		return usuarios.get(username);
	}
	
	@Override
	public void modifyUsuario(String username, Usuario user){
		usuarios.put(username, user);
		/*TODO: Yo validaría que la key username no exista en usuarios.keySet()
		 antes de hacer el put y si existe, devolver algo como nombre de usuario no disponible
		 Sin validar eso, cualquier usuario podría destruir otro usuario cambiando de nombre
	     Lo mismo aplica para createUsuario
		*/
	}
	
	@Override
	public void createUsuario(String username, Usuario user){
		usuarios.put(username, user);
	}
	
	

}
