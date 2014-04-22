package com.tacs.deathlist.domain;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	
	private String nombre;
	private List<Lista> listas;
	
	public Usuario(String username) {
		this.nombre = username;
		this.listas = new ArrayList<Lista>();
	}
	
	public String getUsername() {
		return this.nombre;
	}
	
    public int size() {
        return this.listas.size();
    }
    
    public void modifyUsername(String nombreACambiar) {
    	this.nombre = nombreACambiar;
    }
    
    public void agregarLista(String nombreNuevaLista) {
    	Lista nuevaLista = new Lista(nombreNuevaLista);
    	this.listas.add(nuevaLista);
    }
    
    public boolean existeLista(String nombreLista) {
    	return listas.contains(new Lista(nombreLista));    	
    }
}
