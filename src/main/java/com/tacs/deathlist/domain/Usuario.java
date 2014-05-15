package com.tacs.deathlist.domain;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	private String uid;
	private String nombre;
	private String token;
	private List<String> listas = new ArrayList<String>();

	public Usuario(String username, String uid, String token) {
		this.nombre = username;
		this.uid = uid;
		this.token = token;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
    public List<String> getListas() {
        return listas;
    }

    public boolean existeLista(String nombreLista) {
    	return listas.contains(nombreLista);
    }
    
	public void agregarLista(String nombreLista) {

		if (this.existeLista(nombreLista))
			throw new CustomForbiddenException("El usuario " + this.getNombre()
					+ " ya tiene una lista con el nombre " + nombreLista + ".");
		this.listas.add(nombreLista);
	}
    
    public void eliminarLista(String nombreLista){
        
    	if(!listas.remove(nombreLista))
    		throw new CustomNotFoundException("El usuario " + this.getNombre() + " no tiene una lista con el nombre " + nombreLista + ".");
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }
    
}
