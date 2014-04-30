package com.tacs.deathlist.Listas;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacs.deathlist.Main;
import com.tacs.deathlist.domain.Lista;

public class ListasHelper {
    
    private WebTarget target;
    private Gson gson = new Gson();

    public ListasHelper() {
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    public Lista getListaParseada(String username, String nombreLista) {
        String response = target.path("/users/" + username + "/lists/" + nombreLista).request().get(String.class);
        assertNotNull(response);
        return gson.fromJson(response, Lista.class);
    }

    public Response getLista(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).request().get();
    }

    public Response createList(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).
                request().post(null);
    }
    
    public List<Lista> getListas(String username) {
        String response = target.path("/users/" + username + "/lists").request().get(String.class);
        
        assertNotNull(response);
        Type type = new TypeToken<List<Lista>>(){}.getType();
        return gson.fromJson(response, type);
    }
    
    public Response deleteList(String username, String nombreLista) {
        return target.path("/users/" + username + "/lists/" + nombreLista).request().delete();
    }

}
