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
        
    	if (!this.existeElUsuario(uid))
    		throw new CustomNotFoundException("El usuario de uid = " + uid + " no existe");
    	
    	return (Usuario) objetifyService.load(uid, Usuario.class);
    }

    @Override
    public void createUsuario(Usuario user) {
        if (this.existeElUsuario(user.getUid()))
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el uid " + user.getUid() + ".");
        
        objetifyService.save(user);
    }

    @Override
    public void deleteUsuario(String uid) {
        if (!existeElUsuario(uid)){
            throw new CustomNotFoundException
                    ("el usuario " + uid + " no existe");
        }
        objetifyService.delete(uid, Usuario.class);
    }
    
    private boolean existeElUsuario(String uid) {
    	Usuario usuario = (Usuario) objetifyService.load(uid, Usuario.class);
    	
    	if (usuario == null)
    		return false;
    	
    	return true;
    	
    }
}
