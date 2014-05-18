package com.tacs.deathlist.lista;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.junit.Assert.*;

public class ListasEndToEndTest extends DeathListTest{

    private static final String UID = "1234";


    @Override
    public void setUp() throws Exception {
        super.setUp();
        target("/users/" + UID).
                request().post(Entity.json(new UserCreationRequest("cosme fulanito")));
    }

    public void tearDown() throws Exception  {
        target("/users/" + UID).request().delete();
        super.tearDown();
    }

    @Test
    public void crearListaYChequearQueExista() {
        String lista1 = "Paises";
        String uri = "/users/" + UID + "/lists/" + lista1;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        Lista lista = target(uri).request().get(Lista.class);
        Usuario usuario = target("/users/" + UID).request().get(Usuario.class);


        assertNotNull(lista);
        assertNotNull(usuario);
        assertEquals("el nombre de la lista es erroneo", lista1, lista.getNombre());
        assertTrue("el usuario no tiene la lista", usuario.getListas().contains(lista1));

        target(uri).request().delete();
    }

    @Test
    public void crearVariasListasYChequearQueExistan() {
        String lista1 = "Paises";
        String uri1 = "/users/" + UID + "/lists/" + lista1;
        String lista2 = "Equipos";
        String uri2 = "/users/" + UID + "/lists/" + lista2;
        String lista3 = "Ciudades";
        String uri3 = "/users/" + UID + "/lists/" + lista3;

        Response response = target(uri1).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        response = target(uri2).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        response = target(uri3).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());

        @SuppressWarnings("unchecked")
        List<String> listas = target("/users/" + UID + "/lists").request().get(List.class);
        Usuario usuario = target("/users/" + UID).request().get(Usuario.class);

        assertNotNull(listas);
        assertNotNull(usuario);
        assertEquals("Las listas en la respuesta no son correctas", 3, listas.size());
        assertTrue(listas.contains(lista1));
        assertTrue(listas.contains(lista2));
        assertTrue(listas.contains(lista3));
        assertTrue("el usuario no tiene la lista1", usuario.getListas().contains(lista1));
        assertTrue("el usuario no tiene la lista2", usuario.getListas().contains(lista2));
        assertTrue("el usuario no tiene la lista3", usuario.getListas().contains(lista3));

        target(uri1).request().delete();
        target(uri2).request().delete();
        target(uri3).request().delete();
    }

    @Test
    public void crearListaYEliminarla() {
        String lista1 = "Paises";
        String uri = "/users/" + UID + "/lists/" + lista1;

        Response response = target(uri).request().post(null);
        checkResponse(response, Status.CREATED.getStatusCode());
        response = target(uri).request().get();
        checkResponse(response, Status.OK.getStatusCode());
        response = target(uri).request().delete();
        checkResponse(response, Status.OK.getStatusCode());
        response = target(uri).request().get();
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void crearListaRepetidaYChequearProhibicion() {
        String lista1 = "Paises";
        String uri = "/users/" + UID + "/lists/" + lista1;
        checkResponse(target(uri).request().post(null), Status.CREATED.getStatusCode());
        checkResponse(target(uri).request().post(null), Status.FORBIDDEN.getStatusCode());

        target(uri).request().delete();
    }
    
    @Test
    public void eliminarListaQueNoExiste() {
        String uri = "/users/" + UID + "/lists/" + "listafantasma";

        Response response = target(uri).request().delete();
        checkResponse(response, Status.NOT_FOUND.getStatusCode());
    }

    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
