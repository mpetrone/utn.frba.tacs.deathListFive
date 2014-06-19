package com.tacs.deathlist.service;

import com.tacs.deathlist.domain.Usuario;

import java.util.List;

public interface UserService {

    Usuario getUsuario(String requestorToken, String uid);
    
    Usuario getUsuarioRequestor(String requestorToken, String uid);

    void createUsuario(String requestorToken, String uid);

    void deleteUsuario(String requestorToken, String uid);

    List<Usuario> getFriends(String requestorToken, String uid);

    void validateToken(String token);
    
    void enviarNotificacion(String uidReceptor, String mensaje);
	
	void publicarEnNewsfeed(String uid, String mensaje);

}
