package com.tacs.deathlist.service;

import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Version stub de la obtencion de users.
 *
 */
public class StubUserService implements UserService{

    @Autowired
    private UsuariosDao usuariosDao;

    @Override
    public List<Usuario> getFriends(String token, String uid) {
        List<Usuario> friends = new ArrayList<>();
        friends.add(new Usuario("1111", "userFriend1"));
        friends.add(new Usuario("2222", "userFriend2"));
        return friends;
    }

    @Override
    public Usuario getUsuario(String requestorToken, String uid) {
        return usuariosDao.getUsuario(uid);
    }

    @Override
    public void createUsuario(String requestorToken, String uid) {
        usuariosDao.createUsuario(new Usuario(uid, "nombre"));
    }

    @Override
    public void deleteUsuario(String requestorToken, String uid) {
        usuariosDao.deleteUsuario(uid);
    }

    @Override
    public void validateToken(String token) {
        //todos son validos
    }

    @Override
	public void enviarNotificacion(String uidReceptor, String mensaje) {

	}

	@Override
	public void publicarEnNewsfeed(String uid, String mensaje) {

	}
}
