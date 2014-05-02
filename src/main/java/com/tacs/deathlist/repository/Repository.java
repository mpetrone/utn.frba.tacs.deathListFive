package com.tacs.deathlist.repository;

import java.util.List;

import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;

/**
 * 
 * TODO: Primera version del repositorio integrado,
 * cuando lo integremos con spring podemos separarlo por negocio.
 * 
 * @author matias.petrone
 *
 */
public interface Repository {
	
	Usuario getUsuario(String username);
	
	void createUsuario(String username, Usuario user);
	
	void deleteUsuario(String username);
	
    List<Lista> getAllLists(String username);
    
    Lista getLista(String username, String nombreLista);
    
    void createLista(String username, String nombreLista);

    void deleteLista(String username, String nombreLista);
    
    void createItem(String username, String nombreLista, String itemName);

    void deteleItem(String username, String nombreLista, String itemName);
    
    void voteItem(String username, String nombreLista, String itemName);
}