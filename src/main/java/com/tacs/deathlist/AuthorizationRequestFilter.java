package com.tacs.deathlist;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        //TODO: solo estamos chequeando que venga el token, hay que hacer una implementacion de autenticacion
        if (requestContext.getCookies().get("token") == null) {
            //requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).
              //      entity("No se encontro el token").build());
        }

    }
}
