package com.tacs.deathlist.repository;

import com.tacs.deathlist.domain.Usuario;


public interface UsuariosDao {

    Usuario getUsuario(String uid);

    void createUsuario(String uid, Usuario user);

    void deleteUsuario(String uid);
}
