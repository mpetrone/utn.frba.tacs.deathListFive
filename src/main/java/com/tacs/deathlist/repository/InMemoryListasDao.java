package com.tacs.deathlist.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;

public class InMemoryListasDao implements ListasDao {
    
    @Autowired
    private UsuariosDao usuarioDao;
    
    private Map<String, Lista> listas = new HashMap<String, Lista>();

    @Override
    public List<String> getAllLists(String username) {
        Usuario usuario = usuarioDao.getUsuario(username);
        return usuario.getListas();
    }

    @Override
    public Lista getLista(String username, String nombreLista) {
        if (!listas.containsKey(getKey(username, nombreLista))){
            throw new CustomNotFoundException("La lista " + nombreLista + " del usuario " + username + " no existe");
        }
        return listas.get(getKey(username, nombreLista));
    }

    @Override
    public void createLista(String username, String nombreLista) {
        usuarioDao.getUsuario(username).agregarLista(nombreLista);
        listas.put(getKey(username, nombreLista), new Lista(nombreLista));
    }

    @Override
    public void deleteLista(String username, String nombreLista) {
        usuarioDao.getUsuario(username).eliminarLista(nombreLista);
        listas.remove(getKey(username, nombreLista));
    }

    @Override
    public void createItem(String username, String nombreLista, String itemName) {
        if(!listas.containsKey(getKey(username, nombreLista))){
            throw new CustomNotFoundException("Se quiso agregar el item " + itemName + " a la "
                    + "lista " + nombreLista + " que no existe");
        }
        listas.get(getKey(username, nombreLista)).agregarItem(itemName);
    }

    @Override
    public void deteleItem(String username, String nombreLista, String itemName) {
        getLista(username, nombreLista).eliminarItem(itemName);
    }

    @Override
    public void voteItem(String username, String nombreLista, String itemName) {
        if(!listas.containsKey(getKey(username, nombreLista))){
            throw new CustomNotFoundException("Se quiso votar el item " + itemName + " de la "
                    + "lista " + nombreLista + " que no existe");
        }
        listas.get(getKey(username, nombreLista)).votarItem(itemName);
    }
    
    private String getKey(String username, String nombreLista) {
        return username + "-" + nombreLista;
    }
}
