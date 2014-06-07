package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryListasDao implements ListasDao {
    
    @Autowired
    private UsuariosDao usuarioDao;
    
    private Map<String, Lista> listas = new HashMap<String, Lista>();

    @Override
    public List<String> getAllLists(String uid) {
        Usuario usuario = getUsuario(uid);
        return usuario.getListas();
    }

    @Override
    public Lista getLista(String uid, String nombreLista) {
        if (!listas.containsKey(getKey(uid, nombreLista))){
            throw new CustomNotFoundException("La lista " + nombreLista + " del usuario " + uid + " no existe");
        }
        return listas.get(getKey(uid, nombreLista));
    }

    @Override
    public void createLista(String uid, String nombreLista) {
        getUsuario(uid).agregarLista(nombreLista);
        listas.put(getKey(uid, nombreLista), new Lista(getKey(uid, nombreLista), nombreLista));
    }

    @Override
    public void deleteLista(String uid, String nombreLista) {
        getUsuario(uid).eliminarLista(nombreLista);
        listas.remove(getKey(uid, nombreLista));
    }

    @Override
    public void createItem(String uid, String nombreLista, String itemName) {
        if(!listas.containsKey(getKey(uid, nombreLista))){
            throw new CustomNotFoundException("Se quiso agregar el item " + itemName + " a la "
                    + "lista " + nombreLista + " que no existe");
        }
        listas.get(getKey(uid, nombreLista)).agregarItem(itemName);
    }

    @Override
    public void deleteItem(String uid, String nombreLista, String itemName) {
        getLista(uid, nombreLista).eliminarItem(itemName);
    }

    @Override
    public void voteItem(String uid, String nombreLista, String itemName) {
        if(!listas.containsKey(getKey(uid, nombreLista))){
            throw new CustomNotFoundException("Se quiso votar el item " + itemName + " de la "
                    + "lista " + nombreLista + " que no existe");
        }
        listas.get(getKey(uid, nombreLista)).votarItem(itemName);
    }
    
    private String getKey(String uid, String nombreLista) {
        return uid + "-" + nombreLista;
    }

    private Usuario getUsuario(String uid) {
        Usuario usuario = usuarioDao.getUsuario(uid);
        if(usuario == null) {
            throw new CustomNotFoundException("el usuario " + uid + " no existe");
        }
        return usuario;
    }
}
