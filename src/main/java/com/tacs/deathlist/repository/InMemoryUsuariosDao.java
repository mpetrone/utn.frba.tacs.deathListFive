package com.tacs.deathlist.repository;

import java.util.HashMap;
import java.util.Map;

import com.tacs.deathlist.domain.CustomForbiddenException;
import com.tacs.deathlist.domain.CustomNotFoundException;
import com.tacs.deathlist.domain.Usuario;

public class InMemoryUsuariosDao implements UsuariosDao {

    private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();

    @Override
    public Usuario getUsuario(String username) {
        Usuario usuario = usuarios.get(username);
        if (usuario == null){
            throw new CustomNotFoundException("El usuario " + username
                    + " no existe en el sistema.");
        }
        return usuario;
    }

    @Override
    public void createUsuario(String username, Usuario user) {
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
