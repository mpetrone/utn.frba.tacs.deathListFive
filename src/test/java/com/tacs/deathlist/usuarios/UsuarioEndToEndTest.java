package com.tacs.deathlist.usuarios;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.tacs.deathlist.Main;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.endpoints.resources.UserCreationRequest;

public class UsuarioEndToEndTest {

    private static HttpServer server;
    private static UsuariosHelper userHelper = new UsuariosHelper();

    @BeforeClass
    public static void setUp() throws Exception {
        server = Main.startServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdownNow();
    }
    
    @Test
    public void crearUnUsuarioYChequearQueExista() {
        String username = "john snow";
        String uid = "1234";
        String token = "a token";
        UserCreationRequest request = new UserCreationRequest(uid, token);
        
        checkResponse(userHelper.createUser(username, request), Status.CREATED.getStatusCode());
        Usuario user = userHelper.getUserParseado(username);
        
        assertNotNull(user);
        assertEquals("el username es invalido", username, user.getNombre());
        assertEquals("el uid es invalido", uid, user.getUid());
        assertEquals("el token es invalido", token, user.getToken());
        
        userHelper.deleteUser("john snow");
    }
    
    @Test
    public void crearUsuarioYEliminarloYChequamosQueNoExista() {
        String username = "john snow";
        String uid = "1234";
        String token = "a token";
        UserCreationRequest request = new UserCreationRequest(uid, token);
        
        checkResponse(userHelper.createUser(username, request), Status.CREATED.getStatusCode());
        checkResponse(userHelper.getUser(username), Status.OK.getStatusCode());
        checkResponse(userHelper.deleteUser(username), Status.OK.getStatusCode());
        checkResponse(userHelper.getUser(username), Status.NOT_FOUND.getStatusCode());
    }
    
    
    private void checkResponse(Response response, int statusCode) {
        assertNotNull(response);
        assertEquals(statusCode, response.getStatus());
    }
}
