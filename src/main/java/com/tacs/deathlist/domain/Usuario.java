package com.tacs.deathlist.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
	
	public String getUsername() {
		return this.nombre;
	}
	
    public List<Lista> getListas() {
        return new ArrayList<Lista>(listas.values());
    }

    public void modifyUsername(String nombreACambiar) {
    	this.nombre = nombreACambiar;
    }
    
    public void agregarLista(String nombreNuevaLista) {
    	this.listas.put(nombreNuevaLista, new Lista(nombreNuevaLista));
    }
    
    public boolean existeLista(String nombreLista) {
    	return listas.containsKey(nombreLista);
    }
    
    public Lista getLista(String nombreLista){
        return listas.get(nombreLista);
    }
    
    public void eliminarLista(String nombreLista){
        listas.remove(nombreLista);
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
            .append(nombre, other.getUsername())
            .append(uid, other.getUid())
            .isEquals();
        } else{
            return false;
        }
    }
}
