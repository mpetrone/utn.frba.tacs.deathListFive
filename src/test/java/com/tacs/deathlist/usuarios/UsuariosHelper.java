package com.tacs.deathlist.usuarios;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.tacs.deathlist.Main;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

public class UsuariosHelper {

    private WebTarget target;
    private Gson gson = new Gson();
    
    public UsuariosHelper() {
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }
    
    public Usuario getUserParseado(String username) {
        String response = target.path("/users/" + username).request().get(String.class);
        assertNotNull(response);
        return gson.fromJson(response, Usuario.class);
    }

    public Response getUser(String username) {
        return target.path("/users/" + username).request().get();
    }

    public Response createUser(String username, UserCreationRequest request) {
        return target.path("/users/" + username).
                request().post(Entity.json(gson.toJson(request)));
    }
    
    public Response deleteUser(String username) {
        return target.path("/users/" + username).request().delete();
    }

}
