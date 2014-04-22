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
	}
	
	@Override
	public void createUsuario(String username, Usuario user){
		usuarios.put(username, user);
	}
	
	

}
