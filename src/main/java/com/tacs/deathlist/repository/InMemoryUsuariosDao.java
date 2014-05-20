package com.tacs.deathlist.repository;

import com.tacs.deathlist.domain.CustomForbiddenException;
import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUsuariosDao implements UsuariosDao {

    public static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUsuariosDao.class);
    private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();

    @Override
    public Usuario getUsuario(String uid) {
        LOGGER.debug("Se pidio en la base el usuario: " + uid);
        Usuario usuario = usuarios.get(uid);
        return usuario;
    }

    @Override
    public void createUsuario(String uid, Usuario user) {
        LOGGER.debug("Se va a crear el usuario: " + uid);
        if (usuarios.containsKey(uid)){
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el uid " + uid + ".");
        }
        usuarios.put(uid, user);
    }

	@Override
	public void deleteUsuario(String uid) {
		Usuario eliminado = usuarios.remove(uid);
		if (eliminado == null)
			throw new CustomNotFoundException("El usuario " + uid
					+ " no existe.");
	}

}
