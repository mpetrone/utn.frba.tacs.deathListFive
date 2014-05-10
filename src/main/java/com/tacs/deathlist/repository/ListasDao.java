package com.tacs.deathlist.repository;

import java.util.List;

import com.tacs.deathlist.domain.Lista;

public interface ListasDao {

    List<String> getAllLists(String username);

    Lista getLista(String username, String nombreLista);

    void createLista(String username, String nombreLista);

    void deleteLista(String username, String nombreLista);

    void createItem(String username, String nombreLista, String itemName);

    void deteleItem(String username, String nombreLista, String itemName);

    void voteItem(String username, String nombreLista, String itemName);
}
