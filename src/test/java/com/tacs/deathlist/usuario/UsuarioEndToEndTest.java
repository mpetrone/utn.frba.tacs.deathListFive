package com.tacs.deathlist.usuario;

import com.tacs.deathlist.DeathListTest;
import com.tacs.deathlist.domain.Usuario;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsuarioEndToEndTest extends DeathListTest{

    /* testeamos distintas acciones que puede realizar
     * un usuario de prueba con uid = "1234".
     */
	
	@Test
    public void crearUnUsuarioYChequearQueExista() {
        String uid = "1234";
        
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.CREATED.getStatusCode());
        Usuario user = target("/users/" + uid).request().get(Usuario.class);
        
        assertNotNull(user);
        assertEquals("el uid es invalido", uid, user.getUid());
        
        target("/users/" + uid).request().delete();
    }
    
    @Test
    public void crearUsuarioYEliminarloYChequarQueNoExista() {
        String uid = "1234";
        
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.CREATED.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().delete(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void crearUsuarioRepetidoYChequearProhibicion() {
        String uid = "1234";

        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.CREATED.getStatusCode());

        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.FORBIDDEN.getStatusCode());
        
        target("/users/" + uid).request().delete();
        
    }
    
    @Test
    public void eliminarUsuarioQueNoExiste() {
    	/* antes era correcto que devuelva Not Found, pero
    	 * ahora lo correcto es Forbidden, porque el usuario
    	 * de prueba (el de uid 1234, dado por el mock de la cookie en StubUserService)
    	 * no tiene permiso para borrar otros usuarios.
    	 */
    	checkResponse(target("/users/" + "fantasma").request().delete(), Status.NOT_FOUND.getStatusCode());
    }
        
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
