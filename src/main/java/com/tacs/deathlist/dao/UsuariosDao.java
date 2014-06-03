package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Usuario;


public interface UsuariosDao {

    Usuario getUsuario(String uid);

    void createUsuario(Usuario user);

    void deleteUsuario(String uid);
}
