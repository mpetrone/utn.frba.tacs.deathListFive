package com.tacs.deathlist.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Usuario {
	
	private String nombre;
	private String uid;
    private String token;
	private Map<String,Lista> listas;
	
	public Usuario(String username, String uid, String token) {
		this.nombre = username;
		this.listas = new HashMap<String, Lista>();
		this.uid = uid;
		this.token = token;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
    public List<Lista> getListas() {
        return new ArrayList<Lista>(listas.values());
    }

    public void modifyUsername(String nombreNuevo) {
    	this.nombre = nombreNuevo;
    }
    
    public boolean existeLista(String nombreLista) {
    	return listas.containsKey(nombreLista);
    }
    
    public void agregarLista(String nombreLista) {
    	
    	if(this.existeLista(nombreLista))
    		throw new CustomForbiddenException("El usuario " + this.getNombre() + " ya tiene una lista con el nombre " + nombreLista + ".");
    	
    	this.listas.put(nombreLista, new Lista(nombreLista));
    }
        
    public Lista getLista(String nombreLista){
        
    	Lista lista = listas.get(nombreLista);
    	
    	if (lista == null)
    		throw new CustomNotFoundException("El usuario " + this.getNombre() + " no tiene una lista con el nombre " + nombreLista + ".");
    	
    	return lista;
    }
    
    public void eliminarLista(String nombreLista){
        
    	if(listas.remove(nombreLista) == null)
    		throw new CustomNotFoundException("El usuario " + this.getNombre() + " no tiene una lista con el nombre " + nombreLista + ".");
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        .append(nombre)
        .append(uid)
        .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Lista){
            final Usuario other = (Usuario) obj;
            return new EqualsBuilder()
            .append(nombre, other.getNombre())
            .append(uid, other.getUid())
            .isEquals();
        } else{
            return false;
        }
    }
}
