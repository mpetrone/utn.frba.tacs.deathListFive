package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.ObjectifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GaeListasDao implements ListasDao{

    @Autowired
    private ObjectifyService objectifyService;
    
    @Override
    public List<String> getAllLists(String uid) {
        Usuario usuario = getUsuario(uid);
        return usuario.getListas();
    }

    @Override
    public Lista getLista(String uid, String nombreLista) {
        Lista lista = (Lista) objectifyService.load(getKey(uid,nombreLista), Lista.class);
        if (lista == null){
            throw new CustomNotFoundException("La lista " + nombreLista + " del usuario " + uid + " no existe");
        }
        return lista;
    }

    @Override
    public void createLista(final String uid, final String nombreLista) {
        Usuario usuario = getUsuario(uid);
        usuario.agregarLista(nombreLista);
        objectifyService.save(usuario);
        Lista lista = new Lista(getKey(uid,nombreLista),nombreLista);
        objectifyService.save(lista);
    }

    @Override
    public void deleteLista(final String uid, final String nombreLista) {
        Usuario usuario = getUsuario(uid);
        usuario.eliminarLista(nombreLista);
        objectifyService.save(usuario);
        objectifyService.delete(getKey(uid,nombreLista), Lista.class);
    }

    @Override
    public void createItem(String uid, String nombreLista, String nombreItem) {
        Lista lista = getLista(uid, nombreLista);
        lista.agregarItem(nombreItem);
        objectifyService.save(lista);
    }

    @Override
    public void deleteItem(String uid, String nombreLista, String nombreItem) {
        Lista lista = getLista(uid, nombreLista);
        lista.eliminarItem(nombreItem);
        objectifyService.save(lista);
    }

    @Override
    public void voteItem(String uid, String nombreLista, String nombreItem) {
        Lista lista = getLista(uid, nombreLista);
        lista.votarItem(nombreItem);
        objectifyService.save(lista);
    }

    /**
     * Este método traduce una identificación unívoca de una lista
     * a un formato que actúa como clave en un mapa clave-valor
     * provisto por el servicio de almacenamiento de GAE.
     * 
     * @param uid el id del usuario que posee la lista
     * @param nombreLista el nombre de la lista
     * @return el String asociado a la lista correspondiente
     * 
     */	
    private String getKey(String uid, String nombreLista) {
        return uid + "-" + nombreLista;
    }

    /**
     * Este método obtiene el usuario con un cierto uid.
     * 
     * @param uid El id del usuario
     * @return el usuario con el uid correspondiente
     * @throws CustomNotFoundException si el usuario no existe
     * 
     */	
    private Usuario getUsuario(String uid) {
        Usuario usuario = (Usuario) objectifyService.load(uid, Usuario.class);
        if(usuario == null) {
            throw new CustomNotFoundException("El usuario " + uid + " no existe");
        }
        return usuario;
    }
}
