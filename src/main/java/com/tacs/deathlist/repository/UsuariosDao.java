package com.tacs.deathlist.repository;

import com.tacs.deathlist.domain.Usuario;


public interface UsuariosDao {

    Usuario getUsuario(String username);

    void createUsuario(String username, Usuario user);

    void deleteUsuario(String username);
}
