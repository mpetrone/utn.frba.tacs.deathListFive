package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Lista;

import java.util.List;

public interface ListasDao {

    List<String> getAllLists(String uid);

    Lista getLista(String uid, String nombreLista);

    void createLista(String uid, String nombreLista);

    void deleteLista(String uid, String nombreLista);

    void createItem(String uid, String nombreLista, String itemName);

    void deteleItem(String uid, String nombreLista, String itemName);

    void voteItem(String uid, String nombreLista, String itemName);
}
