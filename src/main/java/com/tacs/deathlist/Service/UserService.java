package com.tacs.deathlist.Service;

import com.tacs.deathlist.domain.Usuario;

import java.util.List;

public interface UserService {

    public Usuario getUser(String token);

    public List<Usuario> getFriends(String token);
}
