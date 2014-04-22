package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Usuario;

public interface UserDao {
	
	Usuario getUsuario(String username);
	
	void modifyUsuario(String username, Usuario user);
	
	void createUsuario(String username, Usuario user);
	
}