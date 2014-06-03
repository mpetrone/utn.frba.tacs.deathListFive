package com.tacs.deathlist.service;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import com.tacs.deathlist.dao.UsuariosDao;
import com.tacs.deathlist.domain.Usuario;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Servicio que provee la informacion de los usuarios de Facebook.
 *
 */
public class FacebookUserService implements UserService {

    @Autowired
    private UsuariosDao usuariosDao;

    @Autowired
    private Ehcache usersCache;

    public Usuario getUser(String token){
        Element element = usersCache.get(token);

        if(element != null){
            return (Usuario) element.getObjectValue();
        }

        FacebookClient facebookClient = new DefaultFacebookClient(token);
        User facebookUser = facebookClient.fetchObject("me", User.class);

        if(facebookUser == null || facebookUser.getId() == null){
            return null;
        }

        Usuario user = new Usuario(facebookUser.getId(), facebookUser.getName());
        Element elementToInsert = new Element(token, user);
        usersCache.put(elementToInsert);
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
