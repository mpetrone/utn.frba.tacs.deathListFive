package com.tacs.deathlist.dao;

import java.util.List;

import com.tacs.deathlist.domain.Lista;

public interface ListDao {
    
    List<Lista> getAllLists();
    
    Lista getLista(String nombreLista);
    
    void createLista(String nombreLista, Lista lista);

    void deleteLista(String nombreLista);
}
