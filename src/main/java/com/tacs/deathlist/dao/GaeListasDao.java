package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.ObjetifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GaeListasDao implements ListasDao{

    @Autowired
    private ObjetifyService objetifyService;

    @Override
    public List<String> getAllLists(String uid) {
        Usuario usuario = getUsuario(uid);
        return usuario.getListas();
    }

    @Override
    public Lista getLista(String uid, String nombreLista) {
        Lista lista = (Lista) objetifyService.load(getKey(uid,nombreLista), Lista.class);
        if (lista == null){
            throw new CustomNotFoundException("La lista " + nombreLista + " del usuario " + uid + " no existe");
        }
        return lista;
    }

    @Override
    public void createLista(final String uid, final String nombreLista) {
        Usuario usuario = getUsuario(uid);
        usuario.agregarLista(nombreLista);
        objetifyService.save(usuario);
        Lista lista = new Lista(getKey(uid,nombreLista),nombreLista);
        objetifyService.save(lista);
    }

    @Override
    public void deleteLista(final String uid, final String nombreLista) {
        Usuario usuario = getUsuario(uid);
        usuario.eliminarLista(nombreLista);
        objetifyService.save(usuario);
        objetifyService.delete(getKey(uid,nombreLista), Lista.class);
    }

    @Override
    public void createItem(String uid, String nombreLista, String itemName) {
        Lista lista = getLista(uid, nombreLista);
        lista.agregarItem(itemName);
        objetifyService.save(lista);
    }

    @Override
    public void deteleItem(String uid, String nombreLista, String itemName) {
        Lista lista = getLista(uid, nombreLista);
        lista.eliminarItem(itemName);
        objetifyService.save(lista);
    }

    @Override
    public void voteItem(String uid, String nombreLista, String itemName) {
        Lista lista = getLista(uid, nombreLista);
        lista.votarItem(itemName);
        objetifyService.save(lista);
    }

    private String getKey(String uid, String nombreLista) {
        return uid + "-" + nombreLista;
    }

    private Usuario getUsuario(String uid) {
        Usuario usuario = (Usuario) objetifyService.load(uid, Usuario.class);
        if(usuario == null) {
            throw new CustomNotFoundException("el usuario " + uid + " no existe");
        }
        return usuario;
    }
}
