package com.tacs.deathlist.Listas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tacs.deathlist.Main;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.usuarios.UsuariosHelper;

public class ListasEndToEndTest {

    private static final String USERNAME = "user1";
    private static HttpServer server;
    private ListasHelper listasHelper = new ListasHelper();
    private static UsuariosHelper usuariosHelper = new UsuariosHelper();

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer();
        usuariosHelper.createUser(USERNAME, new UserCreationRequest("1234", "a token"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        usuariosHelper.deleteUser(USERNAME);
        server.shutdownNow();
    }

    @Test
    public void crearListaYChequearQueExista() {
        String lista1 = "Paises";
        
        Response response = listasHelper.createList(USERNAME, lista1);
        checkResponse(response, Status.CREATED.getStatusCode());
        Lista lista = listasHelper.getListaParseada(USERNAME, lista1);
        
        assertNotNull(lista);
        assertEquals("el nombre de la lista es erroneo", lista1, lista.getNombre());
    }
    
    @Test
    public void crearVariasListasYChequearQueExistan() {
        String lista1 = "Paises";
        String lista2 = "Equipos";
        String lista3 = "Ciudades";
        
        listasHelper.createList(USERNAME, lista1);
        listasHelper.createList(USERNAME, lista2);
        listasHelper.createList(USERNAME, lista3);
        listasHelper.createList("other user", "other list");
        
        List<Lista> listas = listasHelper.getListas(USERNAME);
        
        assertNotNull(listas);
        assertEquals("Las listas en la respuesta no son correctas", 3, listas.size());
        assertTrue(listas.contains(new Lista(lista1)));
        assertTrue(listas.contains(new Lista(lista2)));
        assertTrue(listas.contains(new Lista(lista3)));
    }
    
    @Test
    public void crearListaYEliminarla() {
        String lista1 = "Paises";
        
        checkResponse(listasHelper.createList(USERNAME, lista1), Status.CREATED.getStatusCode());
        checkResponse(listasHelper.getLista(USERNAME, lista1), Status.OK.getStatusCode());
        checkResponse(listasHelper.deleteList(USERNAME, lista1), Status.OK.getStatusCode());
        checkResponse(listasHelper.getLista(USERNAME,lista1), Status.NOT_FOUND.getStatusCode());
    }
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
