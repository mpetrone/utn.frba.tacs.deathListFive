package com.tacs.deathlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tacs.deathlist.domain.Lista;

public class ListasEndToEndTest {

    private static HttpServer server;
    private static WebTarget target;
    private Gson gson = new Gson();

    @BeforeClass
    public static void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdownNow();
    }

// Tests
    
    @Test
    public void crearListaYChequearQueExista() {
        String lista1 = "Paises";
        String item1 = "Argentina";
        String item2 = "Uruguay";
        List<String> itemsToAdd = new ArrayList<String>();
        itemsToAdd.add(item1);
        itemsToAdd.add(item2);
        
        createList(lista1, itemsToAdd);
        Lista lista = getLista(lista1);
        
        assertNotNull(lista);
        assertEquals("el nombre de la lista es erroneo", lista1, lista.getNombre());
        assertEquals("la cantidad de items de la lista es erroneo", 2, lista.size());
        assertTrue("no se encontro el item1 en la lista", lista.existeElItemConNombre(item1));
        assertTrue("no se encontro el item2 en la lista", lista.existeElItemConNombre(item2));
    }
    
    
    @Test
    public void crearVariasListasYChequearQueExistan() {
        String lista1 = "Paises";
        List<String> itemsToAdd = new ArrayList<String>();
        itemsToAdd.add("Argentina");
        itemsToAdd.add("Uruguay");
        
        String lista2 = "Equipos";
        List<String> itemsToAdd2 = new ArrayList<String>();
        itemsToAdd.add("River");
        itemsToAdd.add("Boca");
        
        String lista3 = "Ciudades";
        List<String> itemsToAdd3 = new ArrayList<String>();
        itemsToAdd.add("Buenos Aires");
        itemsToAdd.add("Rosario");
        
        createList(lista1, itemsToAdd);
        createList(lista2, itemsToAdd2);
        createList(lista3, itemsToAdd3);
        
        List<Lista> listas = getListas();
        
        assertNotNull(listas);
        assertEquals("Las listas en la respuesta no son correctas", 3, listas.size());
    }
    
    @Test
    public void crearListaYEliminarla() {
        String lista1 = "Paises";
        String item1 = "Argentina";
        String item2 = "Uruguay";
        List<String> itemsToAdd = new ArrayList<String>();
        itemsToAdd.add(item1);
        itemsToAdd.add(item2);
        
        createList(lista1, itemsToAdd);
        deleteList(lista1);
        
        Response response = target.path("/users/usuario1/lists/" + lista1).request().get();
        assertNotNull(response);
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
 // Helper methods

    private Lista getLista(String nombreLista) {
        String response = target.path("/users/usuario1/lists/" + nombreLista).request().get(String.class);
        
        assertNotNull(response);
        return gson.fromJson(response, Lista.class);
    }

    private void createList(String nombreLista, List<String> items) {
        Response response = target.path("/users/usuario1/lists/" + nombreLista).
                request().post(Entity.json(gson.toJson(items)));
        
        assertNotNull(response);
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
    }
    
    private List<Lista> getListas() {
        String response = target.path("/users/usuario1/lists").request().get(String.class);
        
        assertNotNull(response);
        Type type = new TypeToken<List<Lista>>(){}.getType();
        return gson.fromJson(response, type);
    }
    
    private void deleteList(String lista1) {
        Response response = target.path("/users/usuario1/lists/" + lista1).request().delete();
        
        assertNotNull(response);
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
    
}
