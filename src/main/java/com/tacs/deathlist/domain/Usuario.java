package com.tacs.deathlist.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Unindex;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Cache
public class Usuario implements Serializable {

    @Id
    private String uid;
    private String nombre;
    @Unindex
    private String fechaCreacion;
    @Unindex
    private String fechaLastLogin;
    @Unindex
    private List<String> listas = new ArrayList<>();

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("YYYY/MM/dd HH:mm:ss");
    
    public Usuario(){}

    public Usuario(String uid, String nombre) {
        this.nombre = nombre;
        this.uid = uid;
        DateTime date = new DateTime();        
        this.fechaCreacion = DATE_FORMAT.print(date);
        this.fechaLastLogin = DATE_FORMAT.print(date);
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

    /**
     * Este método crea una nueva lista al usuario.
     * 
     * @param nombreLista el nombre de la lista
     * 
     * @throws CustomNotFoundException si el usuario ya tiene una lista con ese nombre
     * 
     */	
    public void agregarLista(String nombreLista) {

        if (this.existeLista(nombreLista))
            throw new CustomForbiddenException("El usuario " + this.getNombre()
                    + " ya tiene una lista con el nombre " + nombreLista + ".");
        this.listas.add(nombreLista);
    }

    /**
     * Este método elimina una lista de un usuario.
     * 
     * @param itemName el nombre de la lista
     * 
     * @throws CustomNotFoundException si el usuario no tiene una lista con ese nombre
     * 
     */	
    public void eliminarLista(String nombreLista) {

        if (!listas.remove(nombreLista))
            throw new CustomNotFoundException("El usuario " + this.getNombre() + " no tiene una lista con el nombre " + nombreLista + ".");
    }

    public String getUid() {
        return uid;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaLastLogin() {
        return fechaLastLogin;
    }

    public void updateFechaLogin() {
        DateTime date = new DateTime(); 
        this.fechaLastLogin = DATE_FORMAT.print(date);
    }

    @Override
    public String toString() {
        return "Usuario [uid= " + uid + "nombre=" + nombre + ", listas=" + listas + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}
