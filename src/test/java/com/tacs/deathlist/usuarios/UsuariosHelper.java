package com.tacs.deathlist.usuarios;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.tacs.deathlist.CustomResourceConfig;
import com.tacs.deathlist.PropertiesManager;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

public class UsuariosHelper {

    protected WebTarget target;

    public UsuariosHelper(PropertiesManager propertiesManager) {
        Client c = ClientBuilder.newClient(CustomResourceConfig.rc);
        target = c.target(propertiesManager.getProperty("base.uri"));
    }
    
    public Usuario getUserParseado(String username) {
        Usuario response = target.path("/users/" + username).request().get(Usuario.class);
        assertNotNull(response);
        return response;
    }

    public Response getUser(String username) {
        return target.path("/users/" + username).request().get();
    }

    public Response createUser(String username, UserCreationRequest request) {
        return target.path("/users/" + username).
                request().post(Entity.json(request));
    }
    
    public Response deleteUser(String username) {
        return target.path("/users/" + username).request().delete();
    }

}
