package com.tacs.deathlist.usuario;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsuarioEndToEndTest extends DeathListTest{

    @Test
    public void crearUnUsuarioYChequearQueExista() {
        String uid = "1234";
        String nombre = "cosme fulanito";
        UserCreationRequest request = new UserCreationRequest(nombre);
        
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(request)), Status.CREATED.getStatusCode());
        Usuario user = target("/users/" + uid).request().get(Usuario.class);
        
        assertNotNull(user);
        assertEquals("el uid es invalido", uid, user.getUid());
        assertEquals("el nombre es invalido", nombre, user.getNombre());
        
        target("/users/" + uid).request().delete();
    }
    
    @Test
    public void crearUsuarioYEliminarloYChequarQueNoExista() {
        String uid = "1234";
        String nombre = "cosme fulanito";
        UserCreationRequest request = new UserCreationRequest(nombre);
        
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(request)), Status.CREATED.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().delete(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void crearUsuarioRepetidoYChequearProhibicion() {
        String uid = "1234";
        String nombre = "cosme fulanito";
        
        UserCreationRequest request1 = new UserCreationRequest(nombre);
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(request1)), Status.CREATED.getStatusCode());
        
        UserCreationRequest request2 = new UserCreationRequest(nombre);
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(request2)), Status.FORBIDDEN.getStatusCode());
        
        target("/users/" + uid).request().delete();
        
    }
    
    @Test
    public void eliminarUsuarioQueNoExiste() {
    	checkResponse(target("/users/" + "fantasma").request().delete(), Status.NOT_FOUND.getStatusCode());
    }
    
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
