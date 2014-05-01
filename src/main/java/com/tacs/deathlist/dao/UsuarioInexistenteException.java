package com.tacs.deathlist.dao;

public class UsuarioInexistenteException extends RuntimeException {
	
	public UsuarioInexistenteException(String msg) {
		super(msg);
	}

}
