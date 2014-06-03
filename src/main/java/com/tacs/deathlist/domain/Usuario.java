package com.tacs.deathlist.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Unindex;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Usuario(){}

    public Usuario(String uid, String nombre) {
        this.nombre = nombre;
        this.uid = uid;
        Date date = new Date();
        this.fechaCreacion = DATE_FORMAT.format(date);
        this.fechaLastLogin = DATE_FORMAT.format(date);
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
        Date date = new Date();
        this.fechaLastLogin = DATE_FORMAT.format(date);
    }

    @Override
    public String toString() {
        return "Usuerio [uid= " + uid + "nombre=" + nombre + ", listas=" + listas + "]";
    }
}
