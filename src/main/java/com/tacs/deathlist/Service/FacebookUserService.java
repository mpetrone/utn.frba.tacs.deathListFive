package com.tacs.deathlist.service;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Servicio que provee la informacion de los usuarios de Facebook.
 *
 */
@Component
public class FacebookUserService implements UserService {

    public static final String CACHE_NAME = "facebookUsersCache";

    @Autowired
    private UsuariosDao usuariosDao;

    private MemcacheService cacheManager = MemcacheServiceFactory.getMemcacheService();

    public Usuario getUser(String token){
        Object element = cacheManager.get(token);

        if(element != null){
            return (Usuario) element;
        }

        FacebookClient facebookClient = new DefaultFacebookClient(token);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        if(facebookUser == null || facebookUser.getId() == null){
            return null;
        }

        Usuario user = new Usuario(facebookUser.getId(), facebookUser.getName());
        cacheManager.put(token, user);
        return user;
    }

    public List<Usuario> getFriends(String token){

        List<Usuario> allFriendsLists = new ArrayList<>();

        FacebookClient facebookClient = new DefaultFacebookClient(token);
        Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class);

        if(myFriends != null && myFriends.getData() != null) for (User friend : myFriends.getData()) {
            String friendId = friend.getId();
            Usuario userFriend = usuariosDao.getUsuario(friendId);
            if (userFriend != null) {
                allFriendsLists.add(userFriend);
            }
        }
        return allFriendsLists;
    }

}
