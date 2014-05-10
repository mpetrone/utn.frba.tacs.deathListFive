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

import com.tacs.deathlist.ApplicationRunner;
import com.tacs.deathlist.PropertiesManager;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.usuarios.UsuariosHelper;

public class ListasEndToEndTest {

    private static final String USERNAME = "user1";
    private static HttpServer server;
    private static ListasHelper listasHelper;
    private static UsuariosHelper usuariosHelper;

    @BeforeClass
    public static void setUp() throws Exception {
        PropertiesManager propertiesManager = new PropertiesManager();
        server = ApplicationRunner.startServer();
        usuariosHelper = new UsuariosHelper(propertiesManager);
        usuariosHelper.createUser(USERNAME, new UserCreationRequest("1234", "a token"));
        listasHelper = new ListasHelper(propertiesManager);
        
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
        Usuario usuario = usuariosHelper.getUserParseado(USERNAME);
        
        
        assertNotNull(lista);
        assertNotNull(usuario);
        assertEquals("el nombre de la lista es erroneo", lista1, lista.getNombre());
        assertTrue("el usuario no tiene la lista", usuario.getListas().contains(lista1));
        
        listasHelper.deleteList(USERNAME, "Paises");
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
        
        List<String> listas = listasHelper.getListasDelUsuario(USERNAME);
        Usuario usuario = usuariosHelper.getUserParseado(USERNAME);
        
        assertNotNull(listas);
        assertNotNull(usuario);
        assertEquals("Las listas en la respuesta no son correctas", 3, listas.size());
        assertTrue(listas.contains(lista1));
        assertTrue(listas.contains(lista2));
        assertTrue(listas.contains(lista3));
        assertTrue("el usuario no tiene la lista1", usuario.getListas().contains(lista1));
        assertTrue("el usuario no tiene la lista2", usuario.getListas().contains(lista2));
        assertTrue("el usuario no tiene la lista3", usuario.getListas().contains(lista3));
        
        listasHelper.deleteList(USERNAME, "Paises");
        listasHelper.deleteList(USERNAME, "Equipos");
        listasHelper.deleteList(USERNAME, "Ciudades");
    }
    
    @Test
    public void crearListaYEliminarla() {
        String lista1 = "Paises";
        
        checkResponse(listasHelper.createList(USERNAME, lista1), Status.CREATED.getStatusCode());
        checkResponse(listasHelper.getLista(USERNAME, lista1), Status.OK.getStatusCode());
        checkResponse(listasHelper.deleteList(USERNAME, lista1), Status.OK.getStatusCode());
        checkResponse(listasHelper.getLista(USERNAME,lista1), Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void crearListaRepetidaYChequearProhibicion() {
        String lista1 = "Paises";
        
        checkResponse(listasHelper.createList(USERNAME, lista1), Status.CREATED.getStatusCode());
        checkResponse(listasHelper.createList(USERNAME, lista1), Status.FORBIDDEN.getStatusCode());
        
        listasHelper.deleteList(USERNAME, "Paises");
    }
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
