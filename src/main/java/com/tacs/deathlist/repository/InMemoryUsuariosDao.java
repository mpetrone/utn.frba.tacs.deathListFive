package com.tacs.deathlist.repository;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tacs.deathlist.domain.CustomForbiddenException;
import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Usuario;

public class InMemoryUsuariosDao implements UsuariosDao {

    public static final Logger LOGGER = LoggerFactory.getLogger(InMemoryUsuariosDao.class);
    private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();

    @Override
    public Usuario getUsuario(String username) {
        LOGGER.debug("Se pidio en la base el usuario: " + username);
        Usuario usuario = usuarios.get(username);
        if (usuario == null){
            throw new CustomNotFoundException("El usuario " + username
                    + " no existe en el sistema.");
        }
        return usuario;
    }

    @Override
    public void createUsuario(String username, Usuario user) {
        LOGGER.debug("Se va a crear el usuario: " + username);
        if (usuarios.containsKey(username)){
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el nombre " + username + ".");
        }
        usuarios.put(username, user);
    }

    @Override
    public boolean deleteUsuario(String username) {
        return usuarios.remove(username) != null;
    }

}
