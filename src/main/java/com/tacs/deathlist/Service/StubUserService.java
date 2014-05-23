package com.tacs.deathlist.Service;

import com.tacs.deathlist.domain.Usuario;

import java.util.ArrayList;
import java.util.List;

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
}
