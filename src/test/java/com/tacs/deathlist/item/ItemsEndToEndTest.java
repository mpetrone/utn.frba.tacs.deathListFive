package com.tacs.deathlist.item;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Lista;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.*;

public class ItemsEndToEndTest extends DeathListTest{

	private static final String UID = "1";
    private static final String LISTA_NAME = "lista";

    public void setUp() throws Exception {
        super.setUp();
        target("/users/" + UID).
            request().post(Entity.json(null));
        target("/users/" + UID + "/lists/" + LISTA_NAME).request().post(null);
    }

    public void tearDown() throws Exception {
        target("/users/" + UID + "/lists/" + LISTA_NAME).request().delete();
        target("/users/" + UID).request().delete();
        super.tearDown();
    }

    @Test
    public void crearItemYChequearQueExista() {
        String itemName = "perro";
        String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        Lista lista = target("/users/" + UID + "/lists/" + LISTA_NAME).request().get(Lista.class);

        assertNotNull(lista);
        assertTrue("No se encontro el item perro en la lista", lista.existeItem(itemName));
    }

    @Test
    public void crearItemYEliminar() {
        String itemName = "gato";
        String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName;

        checkResponse(target(uri).request().post(null), Status.CREATED.getStatusCode());
        checkResponse(target(uri).request().delete(), Status.OK.getStatusCode());

        Lista lista = target("/users/" + UID + "/lists/" + LISTA_NAME).request().get(Lista.class);
        assertNotNull(lista);
        assertTrue("Se encontro el item gato en la lista", !lista.existeItem(itemName));
    }

    @Test
    public void crearItemYVotar() {
        String itemName = "canario";
        String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        response = target(uri + "/vote").request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());

        Lista lista = target("/users/" + UID + "/lists/" + LISTA_NAME).request().get(Lista.class);

        assertNotNull(lista);
        assertEquals(1, lista.getItem(itemName).getVotos().intValue());
    }
    
    @Test
    public void crearItemRepetidoYChequearProhibicion() {
        String itemName = "elefante";
        String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName;
        
        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        
        response = target(uri).request().post(null);
        checkResponse(response, Status.FORBIDDEN.getStatusCode());
        
    }
    
    @Test
    public void crearItemEnListaQueNoExiste() {
    	String itemName = "Perdido";
    	String uri = "/users/" + UID + "/lists/" + "listafantasma" + "/items/" + itemName;
    	
    	Response response = target(uri).request().post(null);
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void votarItemQueNoExiste() {
    	String itemName = "Perdido";
    	String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName + "/vote";
    	
    	Response response = target(uri).request().post(null);
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void votarItemEnListaQueNoExiste() {
    	String itemName = "Perdido";
    	String uri = "/users/" + UID + "/lists/" + "listafantasma" + "/items/" + itemName + "/vote";
    	
    	Response response = target(uri).request().post(null);
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void eliminarItemQueNoExiste() {
    	String itemName = "Perdido";
    	String uri = "/users/" + UID + "/lists/" + LISTA_NAME + "/items/" + itemName;
    	
    	Response response = target(uri).request().delete();
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }

    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}