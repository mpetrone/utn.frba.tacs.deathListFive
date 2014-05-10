package com.tacs.deathlist.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tacs.deathlist.ApplicationRunner;
import com.tacs.deathlist.PropertiesManager;
import com.tacs.deathlist.Listas.ListasHelper;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.usuarios.UsuariosHelper;

public class ItemsEndToEndTest {

    private static final String USERNAME = "user1";
    private static final String LISTA_NAME = "lista";
    private static HttpServer server;
    private static ItemsHelper itemsHelper;
    private static ListasHelper listasHelper;
    private static UsuariosHelper usuariosHelper;

    @BeforeClass
    public static void setUp() throws Exception {
        PropertiesManager propertiesManager = new PropertiesManager();
        server = ApplicationRunner.startServer();
        listasHelper = new ListasHelper(propertiesManager);
        usuariosHelper = new UsuariosHelper(propertiesManager);
        usuariosHelper.createUser(USERNAME, new UserCreationRequest("1234", "a token"));
        listasHelper.createList(USERNAME, LISTA_NAME);
        itemsHelper = new ItemsHelper(propertiesManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        listasHelper.deleteList(USERNAME, LISTA_NAME);
        usuariosHelper.deleteUser(USERNAME);
        server.shutdownNow();
    }

    @Test
    public void crearItemYChequearQueExista() {
        String itemName = "perro";

        Response response = itemsHelper.createItem(USERNAME, LISTA_NAME, itemName);
        checkResponse(response, Status.CREATED.getStatusCode());
        Lista lista = listasHelper.getListaParseada(USERNAME, LISTA_NAME);

        assertNotNull(lista);
        assertTrue("No se encontro el item perro en la lista", lista.existeItem(itemName));
    }

    @Test
    public void crearItemYEliminar() {
        String itemName = "gato";

        checkResponse(itemsHelper.createItem(USERNAME, LISTA_NAME, itemName), Status.CREATED.getStatusCode());
        checkResponse(listasHelper.getLista(USERNAME,LISTA_NAME), Status.OK.getStatusCode());
        checkResponse(itemsHelper.deleteItem(USERNAME, LISTA_NAME, itemName), Status.OK.getStatusCode());

        Lista lista = listasHelper.getListaParseada(USERNAME,LISTA_NAME);
        assertNotNull(lista);
        assertTrue("Se encontro el item gato en la lista", !lista.existeItem(itemName));
    }

    @Test
    public void crearItemYVotar() {
        String itemName = "canario";

        Response response = itemsHelper.createItem(USERNAME, LISTA_NAME, itemName);
        checkResponse(response, Status.CREATED.getStatusCode());
        itemsHelper.voteItem(USERNAME, LISTA_NAME, itemName);

        Lista lista = listasHelper.getListaParseada(USERNAME,LISTA_NAME);

        assertNotNull(lista);
        assertEquals(1, lista.getItem(itemName).getVotos().intValue());
    }
    
    @Test
    public void crearItemRepetidoYChequearProhibicion() {
        String itemName = "elefante";

        Response response = itemsHelper.createItem(USERNAME, LISTA_NAME, itemName);
        checkResponse(response, Status.CREATED.getStatusCode());
        
        response = itemsHelper.createItem(USERNAME, LISTA_NAME, itemName);
        checkResponse(response, Status.FORBIDDEN.getStatusCode());
        
    }

    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}