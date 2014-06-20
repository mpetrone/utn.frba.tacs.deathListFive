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

    /**
     * Este método obtiene un usuario desde su id.
     * 
     * @param uid el id del usuario
     * 
     * @return el usuario con el id especificado
     * @throws CustomNotFoundException si no existe un usuario con el id especificado
     * 
     */	
    @Override
    public Usuario getUsuario(String uid) {
        
    	if (!this.existeElUsuario(uid))
    		throw new CustomNotFoundException("El usuario de uid = " + uid + " no existe");
    	
    	return (Usuario) objetifyService.load(uid, Usuario.class);
    }

    /**
     * Este método crea un nuevo usuario.
     * 
     * @param user el usuario a crear
     * 
     * @throws CustomForbiddenException si ya existe un usuario con el mismo id
     * 
     */	
    @Override
    public void createUsuario(Usuario user) {
        if (this.existeElUsuario(user.getUid()))
            throw new CustomForbiddenException(
                    "Ya existe un usuario con el uid " + user.getUid() + ".");
        
        objetifyService.save(user);
    }

    /**
     * Este método elimina un usuario.
     * 
     * @param uid el id del usuario a eliminar
     * 
     * @throws CustomNotFoundException si no existe un uuario con el id especificado
     * 
     */	
    @Override
    public void deleteUsuario(String uid) {
        if (!existeElUsuario(uid)){
            throw new CustomNotFoundException
                    ("el usuario " + uid + " no existe");
        }
        objetifyService.delete(uid, Usuario.class);
    }
    
    /**
     * Este método determina si un usuario existe en la base de datos.
     * 
     * @param uid el id del usuario
     * 
     * @return true si existe, false si no existe
     * 
     */	
    private boolean existeElUsuario(String uid) {
    	Usuario usuario = (Usuario) objetifyService.load(uid, Usuario.class);
    	
    	if (usuario == null)
    		return false;
    	
    	return true;
    	
    }
}
