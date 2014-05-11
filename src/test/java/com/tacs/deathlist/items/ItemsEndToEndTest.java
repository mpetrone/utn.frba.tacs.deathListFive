package com.tacs.deathlist.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

public class ItemsEndToEndTest extends DeathListTest{

    private static final String USERNAME = "user1";
    private static final String LISTA_NAME = "lista";

    public void setUp() throws Exception {
        super.setUp();
        target("/users/" + USERNAME).
            request().post(Entity.json(new UserCreationRequest("1234", "a token")));
        target("/users/" + USERNAME + "/lists/" + LISTA_NAME).request().post(null);
    }

    public void tearDown() throws Exception {
        target("/users/" + USERNAME + "/lists/" + LISTA_NAME).request().delete();
        target("/users/" + USERNAME).request().delete();
        super.tearDown();
    }

    @Test
    public void crearItemYChequearQueExista() {
        String itemName = "perro";
        String uri = "/users/" + USERNAME + "/lists/" + LISTA_NAME + "/items/" + itemName;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        Lista lista = target("/users/" + USERNAME + "/lists/" + LISTA_NAME).request().get(Lista.class);

        assertNotNull(lista);
        assertTrue("No se encontro el item perro en la lista", lista.existeItem(itemName));
    }

    @Test
    public void crearItemYEliminar() {
        String itemName = "gato";
        String uri = "/users/" + USERNAME + "/lists/" + LISTA_NAME + "/items/" + itemName;

        checkResponse(target(uri).request().post(null), Status.CREATED.getStatusCode());
        checkResponse(target(uri).request().delete(), Status.OK.getStatusCode());

        Lista lista = target("/users/" + USERNAME + "/lists/" + LISTA_NAME).request().get(Lista.class);
        assertNotNull(lista);
        assertTrue("Se encontro el item gato en la lista", !lista.existeItem(itemName));
    }

    @Test
    public void crearItemYVotar() {
        String itemName = "canario";
        String uri = "/users/" + USERNAME + "/lists/" + LISTA_NAME + "/items/" + itemName;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        response = target(uri + "/vote").request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());

        Lista lista = target("/users/" + USERNAME + "/lists/" + LISTA_NAME).request().get(Lista.class);

        assertNotNull(lista);
        assertEquals(1, lista.getItem(itemName).getVotos().intValue());
    }
    
    @Test
    public void crearItemRepetidoYChequearProhibicion() {
        String itemName = "elefante";
        String uri = "/users/" + USERNAME + "/lists/" + LISTA_NAME + "/items/" + itemName;
        
        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        
        response = target(uri).request().post(null);
        checkResponse(response, Status.FORBIDDEN.getStatusCode());
        
    }

    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}