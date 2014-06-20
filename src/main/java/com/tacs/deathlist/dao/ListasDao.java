package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Lista;

import java.util.List;

public interface ListasDao {

	/**
     * Este método obtiene todas las listas de un usuario.
     * 
     * @param uid El id del usuario
     * @return una lista con los nombres de todas las listas del usuario
     * 
     */	
	List<String> getAllLists(String uid);

	/**
     * Este método obtiene una lista determinada de un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista a obtener
     * @return la lista con el nombre especificado del usuario correspondiente
     * @throws CustomNotFoundException si el usuario no posee una lista con el nombre especificado
     * 
     */	
	Lista getLista(String uid, String nombreLista);

	/**
     * Este método crea una lista vacía a un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista a crear
     * 
     */	
	void createLista(String uid, String nombreLista);

	/**
     * Este método elimina una lista de un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista a eliminar
     * 
     */	
	void deleteLista(String uid, String nombreLista);

	/**
     * Este método crea un nuevo item en una lista de un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista que contendrá el item
     * @param nombreItem el nombre del item a crear
     * 
     */	
	void createItem(String uid, String nombreLista, String nombreItem);

	/**
     * Este método elimina un item en una lista de un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista que contiene el item
     * @param nombreItem el nombre del item a eliminar
     * 
     */	
	void deleteItem(String uid, String nombreLista, String nombreItem);

	/**
     * Este método suma un voto a un item en una lista de un usuario.
     * 
     * @param uid El id del usuario
     * @param nombreLista el nombre de la lista que contiene el item
     * @param nombreItem el nombre del item a votar
     * 
     */	
	void voteItem(String uid, String nombreLista, String nombreItem);
}
