package com.tacs.deathlist.service;

import com.tacs.deathlist.domain.Usuario;

import java.util.List;

public interface UserService {

    public Usuario getUser(String token);

    public List<Usuario> getFriends(String token);
    
    public boolean esAmigoDeUsuario(String token, String uidFriend);
    
    public boolean esElMismoUsuario(String uid1, String uid2);
    
    public void enviarNotificacion(String uidReceptor, String mensaje);
	
	public void publicarEnNewsfeed(String uid, String mensaje);
}
