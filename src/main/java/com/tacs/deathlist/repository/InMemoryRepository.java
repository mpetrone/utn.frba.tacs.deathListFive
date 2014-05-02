package com.tacs.deathlist.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tacs.deathlist.domain.CustomForbiddenException;
import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;

public class InMemoryRepository implements Repository{
    
	private static Map<String, Usuario> usuarios = new HashMap<String, Usuario>();

	@Override
	public Usuario getUsuario(String username) {

		Usuario usuario = usuarios.get(username);

		if (usuario == null)
			throw new CustomNotFoundException("El usuario " + username
					+ " no existe en el sistema.");

		return usuario;
	}

	@Override
	public void createUsuario(String username, Usuario user) {

		if (usuarios.containsKey(username))
			throw new CustomForbiddenException(
					"Ya existe un usuario con el nombre " + username + ".");

		usuarios.put(username, user);
	}

	@Override
	public void deleteUsuario(String username) {
		if (usuarios.remove(username) == null)
			throw new CustomNotFoundException("El usuario " + username
					+ " no existe en el sistema.");
	}

	@Override
	public List<Lista> getAllLists(String username) {

		return this.getUsuario(username).getListas(); // lanza excepcion si el
														// usuario no existe

	}

	@Override
	public Lista getLista(String username, String nombreLista) {

		return this.getUsuario(username).getLista(nombreLista);
		// si el usuario o la lista no existen, se lanza la excepcion
		// correspondiente.
	}

	@Override
	public void createLista(String username, String nombreLista) {

		this.getUsuario(username).agregarLista(nombreLista);
		// si el usuario no existe o la lista es repetida, se lanza la excepcion
		// correspondiente.

	}

	@Override
	public void deleteLista(String username, String nombreLista) {

		this.getUsuario(username).eliminarLista(nombreLista);
		// si el usuario o la lista no existen, se lanza la excepcion
		// correspondiente.
	}

	@Override
	public void createItem(String username, String nombreLista, String itemName) {

		this.getUsuario(username).getLista(nombreLista).agregarItem(itemName);
		/*
		 * si el usuario o la lista no existen, o el item es repetido, o la
		 * lista ya tiene demasiados items, se lanza la excepcion
		 * correspondiente.
		 */
	}

	@Override
	public void deteleItem(String username, String nombreLista, String itemName) {

		this.getUsuario(username).getLista(nombreLista).eliminarItem(itemName);
		// si el usuario, la lista o el item no existen,
		// se lanza la excepcion correspondiente.

	}

	@Override
	public void voteItem(String username, String nombreLista, String itemName) {

		this.getUsuario(username).getLista(nombreLista).votarItem(itemName);
		// si el usuario, la lista o el item no existen,
		// se lanza la excepcion correspondiente.
	}
}
