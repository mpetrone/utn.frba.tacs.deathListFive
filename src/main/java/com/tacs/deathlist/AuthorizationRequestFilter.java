package com.tacs.deathlist;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;


public class AuthorizationRequestFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext)
            throws IOException {
        //TODO: solo estamos chequeando que venga el token, hay que hacer una implementacion de autenticacion
        if (requestContext.getCookies().get("token") == null) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).
                    entity("No se encontro el token").build());
        }

    }
}
