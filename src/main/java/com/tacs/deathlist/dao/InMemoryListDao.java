package com.tacs.deathlist.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tacs.deathlist.domain.Lista;

public class InMemoryListDao implements ListDao {
    
    static private Map<String,Lista> listas = new HashMap<>();

    @Override
    public List<Lista> getAllLists() {
        return new ArrayList<Lista>(listas.values());
    }

    @Override
    public Lista getLista(String nombreLista) {
        return listas.get(nombreLista);
    }

    @Override
    public void createLista(String nombreLista, Lista lista) {
        listas.put(nombreLista, lista);
    }

    @Override
    public void deleteLista(String nombreLista) {
        listas.remove(nombreLista);
    }

}
