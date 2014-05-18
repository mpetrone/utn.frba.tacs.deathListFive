package com.tacs.deathlist.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private String uid;
    private String nombre;
    private String fechaCreacion;
    private String fechaLastLogin;
    private List<String> listas = new ArrayList<>();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

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

}
