package com.tacs.deathlist.usuarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

public class UsuarioEndToEndTest extends DeathListTest{

    @Test
    public void crearUnUsuarioYChequearQueExista() {
        String username = "johnSnow";
        String uid = "1234";
        String token = "a token";
        UserCreationRequest request = new UserCreationRequest(uid, token);
        
        checkResponse(target("/users/" + username).
                request().post(Entity.json(request)), Status.CREATED.getStatusCode());
        Usuario user = target("/users/" + username).request().get(Usuario.class);
        
        assertNotNull(user);
        assertEquals("el username es invalido", username, user.getNombre());
        assertEquals("el uid es invalido", uid, user.getUid());
        assertEquals("el token es invalido", token, user.getToken());
        
        target("/users/" + username).request().delete();
    }
    
    @Test
    public void crearUsuarioYEliminarloYChequarQueNoExista() {
        String username = "john snow";
        String uid = "1234";
        String token = "a token";
        UserCreationRequest request = new UserCreationRequest(uid, token);
        
        checkResponse(target("/users/" + username).
                request().post(Entity.json(request)), Status.CREATED.getStatusCode());
        checkResponse(target("/users/" + username).request().get(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + username).request().delete(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + username).request().get(), Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void crearUsuarioRepetidoYChequearProhibicion() {
        String username = "john snow";
        String uid = "1234";
        String token = "a token";
        
        UserCreationRequest request1 = new UserCreationRequest(uid, token);
        checkResponse(target("/users/" + username).
                request().post(Entity.json(request1)), Status.CREATED.getStatusCode());
        
        UserCreationRequest request2 = new UserCreationRequest(uid, token);
        checkResponse(target("/users/" + username).
                request().post(Entity.json(request2)), Status.FORBIDDEN.getStatusCode());
        
        target("/users/" + username).request().delete();
        
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
