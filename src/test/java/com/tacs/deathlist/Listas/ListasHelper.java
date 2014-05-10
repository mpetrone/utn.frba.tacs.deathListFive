package com.tacs.deathlist.Listas;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.tacs.deathlist.CustomResourceConfig;
import com.tacs.deathlist.PropertiesManager;
import com.tacs.deathlist.domain.Lista;

public class ListasHelper {
    
    protected WebTarget target;

    public ListasHelper(PropertiesManager propertiesManager) {
        Client c = ClientBuilder.newClient(CustomResourceConfig.rc);
        target = c.target(propertiesManager.getProperty("base.uri"));
    }

    public Lista getListaParseada(String username, String nombreLista) {
        Lista response = target.path("/users/" + username + "/lists/" + nombreLista).request().get(Lista.class);
        assertNotNull(response);
        return response;
    }

    public Response getLista(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).request().get();
    }

    public Response createList(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).
                request().post(null);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getListasDelUsuario(String username) {
        List<String> response = target.path("/users/" + username + "/lists").request().get(List.class);
        assertNotNull(response);
        return response;
    }
    
    public Response deleteList(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).request().delete();
    }

}
