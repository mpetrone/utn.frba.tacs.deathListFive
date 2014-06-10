package com.tacs.deathlist.service;

import com.tacs.deathlist.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

/**
 *
 * Version stub de la obtencion de users.
 *
 */
public class StubUserService implements UserService{
	
	@Override
    public Usuario getUser(String token) {
        return new Usuario("1234","user1");
    }

    @Override
    public List<Usuario> getFriends(String token) {
        List<Usuario> friends = new ArrayList<>();
        friends.add(new Usuario("1111", "userFriend1"));
        friends.add(new Usuario("2222", "userFriend2"));
        return friends;
    }
    
    @Override
	public void enviarNotificacion(String uidReceptor, String mensaje) {
		// TODO Auto-generated method stub

	}

	@Override
	public void publicarEnNewsfeed(String uid, String mensaje) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean esAmigoDeUsuario(String token, String uidFriend) {
		
		if (uidFriend == "1111" || uidFriend == "2222")
			return true;
		else
			return false;
	}

	@Override
	public boolean esElMismoUsuario(String uid1, String uid2) {
		
		return uid1 != null && uid1.equalsIgnoreCase(uid2);
	}

	@Override
	public String getTokenInCookies(HttpHeaders hh) {
		
		return "elToken";
	}

	@Override
	public String getUidInCookies(HttpHeaders hh) {
		
		return "1234";
	}
}
