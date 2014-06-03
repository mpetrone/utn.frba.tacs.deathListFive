package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUsuariosDao implements UsuariosDao {

    public static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUsuariosDao.class);
    private Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Usuario getUsuario(String uid) {
        LOGGER.debug("Se pidio en la base el usuario: " + uid);
        return usuarios.get(uid);
    }

    @Override
    public void createUsuario(Usuario user) {
        LOGGER.debug("Se va a crear el usuario: " + user.getUid());
        if (usuarios.containsKey(user.getUid())){
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el uid " + user.getUid() + ".");
        }
        usuarios.put(user.getUid(), user);
    }

	@Override
	public void deleteUsuario(String uid) {
		Usuario eliminado = usuarios.remove(uid);
		if (eliminado == null)
			throw new CustomNotFoundException("El usuario " + uid
					+ " no existe.");
	}

}
