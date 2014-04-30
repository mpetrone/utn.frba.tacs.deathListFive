package com.tacs.deathlist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;

public class InMemoryRepository implements Repository{
    
    private static Map<String,Usuario> map = new HashMap<String, Usuario>();

    @Override
    public Usuario getUsuario(String username) {
        return map.get(username);
    }

    @Override
    public void createUsuario(String username, Usuario user) {
        map.put(username, user);
    }
    
    @Override
    public void deleteUsuario(String username) {
        map.remove(username);
    }

    @Override
    public List<Lista> getAllLists(String username) {
        if(map.containsKey(username)){
            return map.get(username).getListas();
        }
        return null;
    }

    @Override
    public Lista getLista(String username, String nombreLista) {
        if(map.containsKey(username)){
            return map.get(username).getLista(nombreLista);
        }
        return null;
    }

    @Override
    public void createLista(String username, String nombreLista) {
        if(map.containsKey(username)){
            map.get(username).agregarLista(nombreLista);;
        }
    }

    @Override
    public void deleteLista(String username, String nombreLista) {
        if(map.containsKey(username)){
            map.get(username).eliminarLista(nombreLista);;
        }
    }

    @Override
    public void createItem(String username, String nombreLista, String itemName) {
        if(map.containsKey(username)){
            if(map.get(username).existeLista(nombreLista)){
                map.get(username).getLista(nombreLista).agregarItem(itemName);
            }
        }
    }

    @Override
    public void deteleItem(String username, String nombreLista, String itemName) {
        if(map.containsKey(username)){
            if(map.get(username).existeLista(nombreLista)){
                map.get(username).getLista(nombreLista).eliminarItem(itemName);;
            }
        }
        
    }

    @Override
    public void voteItem(String username, String nombreLista, String itemName) {
        if(map.containsKey(username)){
            if(map.get(username).existeLista(nombreLista)){
                map.get(username).getLista(nombreLista).votarItem(itemName);
            }
        }
    }
}
