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

	
	@Test
    public void crearUnUsuarioYChequearQueExista() {
        String uid = "1";
        
        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.CREATED.getStatusCode());
        Usuario user = target("/users/" + uid).request().get(Usuario.class);
        
        assertNotNull(user);
        assertEquals("el uid es invalido", uid, user.getUid());
        
        target("/users/" + uid).request().delete();
    }
    
    @Test
    public void crearUsuarioYEliminarloYChequarQueNoExista() {
        String uid = "1";
        
        checkResponse(target("/users/" + uid).request().post(Entity.json(null)), Status.CREATED.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().delete(), Status.OK.getStatusCode());
        checkResponse(target("/users/" + uid).request().get(), Status.NOT_FOUND.getStatusCode());
    }
    
    @Test
    public void crearUsuarioRepetidoYChequearProhibicion() {
        String uid = "1";

        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.CREATED.getStatusCode());

        checkResponse(target("/users/" + uid).
                request().post(Entity.json(null)), Status.FORBIDDEN.getStatusCode());
        
        target("/users/" + uid).request().delete();
        
    }
    
    @Test
    public void eliminarUsuarioQueNoExiste() {
    	/* La repuesta correcta de la aplicación en este caso es Forbidden,
    	 * ya que el usuario que no existe no es el usuario activo
    	 * (solo se permite eliminar al propio usuario)
    	 */
    	checkResponse(target("/users/" + "fantasma").request().delete(), Status.FORBIDDEN.getStatusCode());
    }
    
    @Test
    public void obtenerListaDeAmigos() {
    	
    	String uid = "1";
    	
    	String uri = "/users/" + uid + "/friends";
    	
    	checkResponse(target(uri).request().get(), Status.OK.getStatusCode());
    }        
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
