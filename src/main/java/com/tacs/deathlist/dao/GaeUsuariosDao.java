package com.tacs.deathlist.dao;

import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomForbiddenException;
import com.tacs.deathlist.domain.exception.CustomNotFoundException;
import com.tacs.deathlist.service.ObjectifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaeUsuariosDao implements UsuariosDao {

    @Autowired
    private ObjectifyService objetifyService;

    @Override
    public Usuario getUsuario(String uid) {
        return (Usuario) objetifyService.load(uid, Usuario.class);
    }

    @Override
    public void createUsuario(Usuario user) {
        if (getUsuario(user.getUid()) != null){
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el uid " + user.getUid() + ".");
        }
        objetifyService.save(user);
    }

    @Override
    public void deleteUsuario(String uid) {
        if (getUsuario(uid) == null){
            throw new CustomNotFoundException
                    ("el usuario " + uid + " no existe");
        }
        objetifyService.delete(uid, Usuario.class);
    }
}
