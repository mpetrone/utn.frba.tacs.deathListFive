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

import com.tacs.deathlist.Main;
import com.tacs.deathlist.Listas.ListasHelper;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import com.tacs.deathlist.usuarios.UsuariosHelper;

public class ItemsEndToEndTest {

    private static final String USERNAME = "user1";
    private static final String LISTA_NAME = "lista";
    private static HttpServer server;
    private ItemsHelper itemsHelper = new ItemsHelper();
    private static ListasHelper listasHelper = new ListasHelper();
    private static UsuariosHelper usuariosHelper = new UsuariosHelper();
    

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer();
        usuariosHelper.createUser(USERNAME, new UserCreationRequest("1234", "a token"));
        listasHelper.createList(USERNAME, LISTA_NAME);
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

    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}